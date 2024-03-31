package org.clipboardshared.manager.ui.activity

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.clipboardshared.manager.R
import org.clipboardshared.manager.databinding.ActivityMainBinding
import org.clipboardshared.manager.databinding.MainContentBinding
import org.clipboardshared.manager.utils.AppUtils
import org.clipboardshared.manager.utils.HapticUtils.hapticConfirm

class MainActivity : AppCompatActivity() {

    private lateinit var _activityMainBinding: ActivityMainBinding
    private val activityMainBinding get() = _activityMainBinding
    private val mainContentBinding: MainContentBinding get() = activityMainBinding.mainContent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inflateView()

        setupEdgeToEdge()

        setupTopAppBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        activityMainBinding
    }

    private fun showAboutDialog() {
        val rootView = MaterialAlertDialogBuilder(this@MainActivity).setView(R.layout.dialog_about).show()
        val versionTextView = rootView.findViewById<TextView>(R.id.version)!!
        val githubSpannableTextView = rootView.findViewById<TextView>(R.id.github)!!

        versionTextView.text = getString(R.string.app_version,"null", "null")
        githubSpannableTextView.text = Html.fromHtml(getString(R.string.app_github), Html.FROM_HTML_MODE_COMPACT)
        githubSpannableTextView.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun setupEdgeToEdge() {
        enableEdgeToEdge()
        if (AppUtils.atLeast(Build.VERSION_CODES.Q)) window.isNavigationBarContrastEnforced = false
    }

    private fun inflateView() {
        _activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
    }

    private fun setupTopAppBar() {
        activityMainBinding.topAppBar.apply {
            setNavigationOnClickListener {
                hapticConfirm(this)
                showAboutDialog()
            }
            setOnMenuItemClickListener { menuItem ->
                hapticConfirm(this)
                false
            }
        }
    }
}
