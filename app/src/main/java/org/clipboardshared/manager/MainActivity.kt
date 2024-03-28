package org.clipboardshared.manager

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.clipboardshared.manager.ui.theme.ClipboardSharedTheme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Set the system bar background to transparent
            WindowCompat.setDecorFitsSystemWindows(window, true)
            ClipboardSharedTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetIpAddressComposable()
                }
            }
        }
    }
}

@Composable
fun SetIpAddressComposable(modifier: Modifier = Modifier) {
    var ipAddress by remember { mutableStateOf(TextFieldValue()) }
    val serverViewModel: ServerViewModel = remember { ServerViewModel() }
    Column(modifier = modifier) {
        TextField(
            value = ipAddress,
            onValueChange = { ipAddress = it },
            label = { Text("Enter server ip") },
            modifier = Modifier.padding(10.dp)
        )
        Button(
            onClick = {
                serverViewModel.serverIpAddress = ipAddress.text
                Log.i("MainActivity", "IP Address: ${serverViewModel.serverIpAddress}")
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Connect")
        }
    }
}

fun sendClipboardData(data: String, ipAddress: String) {
    val url = "http://${ipAddress}/data"
    val json = """
        {
            "data": "${data}"
        }
    """.trimIndent()

    OkHttpClient().postJson(url, json) { responseData ->
        responseData?.let {
            println("Response: $it")
        } ?: run {
            println("Request failed")
        }
    }
}

fun OkHttpClient.postJson(url: String, json: String, callback: (String?) -> Unit) {
    val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())
    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .build()

    newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            // 请求失败处理
            callback(null)
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            // 请求成功处理
            val responseData = response.body?.string()
            callback(responseData)
        }
    })
}