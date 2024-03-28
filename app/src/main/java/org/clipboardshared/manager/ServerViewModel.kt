package org.clipboardshared.manager

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class ServerViewModel : ViewModel() {
    var serverIpAddress by mutableStateOf("")
}
