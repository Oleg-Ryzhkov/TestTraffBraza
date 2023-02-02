package com.example.testtraffbraza

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.StatsLog.logEvent
import android.view.View
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.appsflyer.AppsFlyerLib
import com.onesignal.OneSignal


open class MainActivity : AppCompatActivity() {
    lateinit var mywebview: WebView
    private var mUploadMessage: ValueCallback<Array<Uri>>? = null
    private val FILECHOOSER_RESULTCODE = 0
    var link = "https://fex.net/"
    var savurl = ""
    val appssflay = "GwybXxkTG7RrrxHVLePMpN"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val OneSignalId = "5c45cac7-98a4-4c5d-956a-9cd521a664ea"
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId(OneSignalId)
        AppsFlyerLib.getInstance().init("$appssflay", null, this)
        AppsFlyerLib.getInstance().start(this)
        mywebview = findViewById(R.id.webview)
        AFApplication().AppsFlyer()

        @SuppressLint("SetJavaScriptEnabled")
        fun CreatWeb() {
            var savedLink = getSharedPreferences(packageName, MODE_PRIVATE).getString("saveurl", null)
            if (savedLink == null){
                mywebview.loadUrl(link)
                Log.e("appsflay","Company Name")
            }
            else mywebview.loadUrl(savedLink)

            mywebview.visibility = View.VISIBLE
            mywebview.settings.apply {
                setAppCacheEnabled(true)
                allowFileAccessFromFileURLs = true
                saveFormData = true
                mixedContentMode = 0
                savePassword = true
                allowContentAccess = true
                setRenderPriority(WebSettings.RenderPriority.HIGH)
                setEnableSmoothTransition(true)
                loadsImagesAutomatically = true
                allowUniversalAccessFromFileURLs = true
                databaseEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                javaScriptCanOpenWindowsAutomatically = true
                domStorageEnabled = true
                allowFileAccess = true
                javaScriptEnabled = true
                mywebview.webChromeClient = webChrome

            }
        }

        CreatWeb()
    }

    private val webChrome = object : WebChromeClient() {
        override fun onShowFileChooser(
            webView: WebView,
            filePathCallback: ValueCallback<Array<Uri>>,
            fileChooserParams: FileChooserParams
        ): Boolean {
            if (mUploadMessage != null) {
                mUploadMessage!!.onReceiveValue(null)
                mUploadMessage = null
            }
            mUploadMessage = filePathCallback
            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.addCategory(Intent.CATEGORY_OPENABLE)
            i.type = "*/*"
            startActivityForResult(
                Intent.createChooser(i, "*/* "),
                FILECHOOSER_RESULTCODE
            )
            return true
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage) return
            if (intent == null || resultCode != RESULT_OK) null else intent.data
            mUploadMessage!!.onReceiveValue(
                WebChromeClient.FileChooserParams.parseResult(
                    resultCode,
                    data
                )
            )
            mUploadMessage = null

        }

    }

        override fun onBackPressed() {
            if (mywebview.isFocusable && mywebview.canGoBack()) {
                mywebview.goBack()
            }

        }

    override fun onPause() {
        super.onPause()
        savurl = mywebview.url.toString()
        getSharedPreferences(packageName, MODE_PRIVATE).edit().putString("saveurl",savurl).apply()
    }
    }
