package com.anatame.pickaflix.common.utils

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.lifecycle.MutableLiveData
import java.io.ByteArrayInputStream
import java.io.InputStream

@SuppressLint("SetJavaScriptEnabled")
class EpsWebViewHelper(
    private val epsPlayer: WebView,
    private val vidEmbedURl: String
) {
    val streamUrl: MutableLiveData<String> = MutableLiveData()

    init {
        webViewLayerType()
        epsPlayer.settings.userAgentString =
            "Mozilla/5.0 (Linux; Android 7.0; SM-G930V Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.125 Mobile Safari/537.36"
        epsPlayer.settings.javaScriptEnabled = true
        epsPlayer.settings.domStorageEnabled = true
        epsPlayer.settings.cacheMode = WebSettings.LOAD_DEFAULT
        epsPlayer.settings.databaseEnabled = true
        epsPlayer.settings.mediaPlaybackRequiresUserGesture = false

        val map = HashMap<String, String>()
        map["referer"] = "https://fmoviesto.cc"

        epsPlayer.loadUrl(vidEmbedURl, map)

        epsPlayer.webViewClient = object : WebViewClient() {

            override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
                if (BlockHosts().hosts.contains(request!!.url.host)) {
                    val textStream: InputStream = ByteArrayInputStream("".toByteArray())
                    return getTextWebResource(textStream)
                }
                return super.shouldInterceptRequest(view, request)
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
                if (url != null) {
                    if(url.endsWith("playlist.m3u8")){
                        streamUrl.postValue(url!!)
                    }
                }
            }
        }
    }

    private fun getTextWebResource(data: InputStream): WebResourceResponse {
        return WebResourceResponse("text/plain", "UTF-8", data);
    }

    private fun webViewLayerType() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            epsPlayer.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            epsPlayer.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }
}