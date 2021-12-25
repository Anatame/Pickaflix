package com.anatame.pickaflix.presentation

import android.app.Activity
import android.app.PictureInPictureParams
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.*
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.anatame.pickaflix.R
import com.anatame.pickaflix.common.utils.BlockHosts
import com.anatame.pickaflix.databinding.ActivityPlayerBinding
import com.anatame.pickaflix.databinding.FragmentMovieDetailBinding
import com.anatame.pickaflix.presentation.Fragments.Detail.MovieDetailFragment
import java.io.ByteArrayInputStream
import java.io.InputStream
import android.view.WindowInsetsController

import android.view.WindowInsets
import androidx.annotation.RequiresApi


class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private val context: Context = this@PlayerActivity
    
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        goFullScreen()

        val extras = intent.extras
        if (extras != null) {
            val url = extras.getString("vidUrl")
            if (url != null) {
                loadEpsPlayer(url)
            }
        }

        binding.btnEnterPip.setOnClickListener {
            this.enterPictureInPictureMode(PictureInPictureParams.Builder().build())

        }
    }

    override fun onStart() {
        super.onStart()
        goFullScreen()
    }

    override fun onResume() {
        super.onResume()
        goFullScreen()
    }

    private fun loadEpsPlayer(vidEmbedURl: String = "https://streamrapid.ru/embed-4/FZbgGAE8iDRR?z="){
        binding.apply {
            epsPlayer.webViewClient = WebViewClient()
            epsPlayer.settings.javaScriptEnabled = true
            epsPlayer.settings.userAgentString =
                "Mozilla/5.0 (Linux; Android 7.0; SM-G930V Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.125 Mobile Safari/537.36"

            epsPlayer?.settings?.userAgentString = "Mozilla/5.0 (Linux; Android 7.0; SM-G930V Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.125 Mobile Safari/537.36"
            epsPlayer.settings.setDomStorageEnabled(true);
            epsPlayer.settings.cacheMode = WebSettings.LOAD_DEFAULT
            epsPlayer.settings.setAppCacheEnabled(true);
            epsPlayer.settings.setAppCachePath(context.filesDir.absolutePath + "/cache");
            epsPlayer.settings.databaseEnabled = true;
            epsPlayer.settings.setDatabasePath(context.filesDir.absolutePath + "/databases");
            epsPlayer.settings.mediaPlaybackRequiresUserGesture = false;

            val map = HashMap<String, String>()
            map.put("referer", "https://fmoviesto.cc")

            epsPlayer.loadUrl(
                vidEmbedURl,
                map
            )


            epsPlayer.addJavascriptInterface(
                WebAppInterface(context), "Android")

            fun getTextWebResource(data: InputStream): WebResourceResponse {
                return WebResourceResponse("text/plain", "UTF-8", data);
            }

            epsPlayer.webViewClient = object : WebViewClient() {

                override fun shouldInterceptRequest(
                    view: WebView?,
                    request: WebResourceRequest?
                ): WebResourceResponse? {

                    if (BlockHosts().hosts.contains(request!!.url.host)) {
                        val textStream: InputStream = ByteArrayInputStream("".toByteArray())
                        return getTextWebResource(textStream)
                    }
                    return super.shouldInterceptRequest(view, request)
                }

                override fun onLoadResource(view: WebView?, url: String?) {
                    super.onLoadResource(view, url)
                    if(url!!.endsWith("playlist.m3u8")){
                        Log.d("movieSeasons", url)
                    }
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    epsPlayer.loadUrl(
                        """javascript:(function f() {
                            let myInterval = setInterval(() => {
                            let fBtn = document.querySelector('.jw-icon-fullscreen');
                            if(fBtn != null || fBtn != 'undefined'){
                                 document.querySelector('.jw-svg-icon-fullscreen-off').style.display = 'block';
                                 document.querySelector('.jw-svg-icon-fullscreen-on').style.display = 'none';
                                  document.querySelector('.jw-icon-fullscreen').addEventListener('click', function() {
                                     Android.finish();
                                 });
                                   clearInterval(myInterval);
                            }
                        }, 200);
                        
                           let overlayInterval = setInterval(() => {
                            let overlay = document.getElementById('overlay-center');
                            if(overlay != null || overlay != 'undefined'){
                              document.getElementById('overlay-center').remove();
                               clearInterval(overlayInterval);
                            }
                        }, 200);
          
                      })()""".trimIndent().trimMargin()
                    );
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)

                    epsPlayer.visibility = View.VISIBLE
                }

            }


        }
    }

    /** Instantiate the interface and set the context  */
    class WebAppInterface(
        private val mContext: Context,
    ) {

        /** Show a toast from the web page  */
        @JavascriptInterface
        fun finish() {
            Toast.makeText(mContext, "clicked", Toast.LENGTH_SHORT).show()
            (mContext as Activity).finish()
        }
    }




    private fun goFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            val controller = window.insetsController
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

    }
}