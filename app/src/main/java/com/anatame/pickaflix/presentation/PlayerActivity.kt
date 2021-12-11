package com.anatame.pickaflix.presentation

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.Toast
import com.anatame.pickaflix.R
import com.anatame.pickaflix.common.utils.BlockHosts
import com.anatame.pickaflix.databinding.ActivityPlayerBinding
import com.anatame.pickaflix.databinding.FragmentMovieDetailBinding
import com.anatame.pickaflix.presentation.Fragments.Detail.MovieDetailFragment
import java.io.ByteArrayInputStream
import java.io.InputStream

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private val context: Context = this@PlayerActivity
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        
        val extras = intent.extras
        if (extras != null) {
            val url = extras.getString("vidUrl")
            Toast.makeText(this@PlayerActivity, url.toString(), Toast.LENGTH_SHORT).show()
            if (url != null) {
                loadEpsPlayer(url)
            }
        }
    }

    private fun loadEpsPlayer(vidEmbedURl: String = "https://streamrapid.ru/embed-4/FZbgGAE8iDRR?z="){
        binding.apply {
            epsPlayer.webViewClient = WebViewClient()
            epsPlayer.settings.javaScriptEnabled = true
            epsPlayer.settings.userAgentString =
                "Mozilla/5.0 (Linux; Android 7.0; SM-G930V Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.125 Mobile Safari/537.36"

            epsPlayer?.settings?.userAgentString = "Mozilla/5.0 (Linux; Android 7.0; SM-G930V Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.125 Mobile Safari/537.36"
            val map = HashMap<String, String>()
            map.put("referer", "https://fmoviesto.cc")

            epsPlayer.loadUrl(
                vidEmbedURl,
                map
            )

            epsPlayer.settings.setDomStorageEnabled(true);
            epsPlayer.settings.cacheMode = WebSettings.LOAD_DEFAULT
            epsPlayer.settings.setAppCacheEnabled(true);
            epsPlayer.settings.setAppCachePath(context.filesDir.absolutePath + "/cache");
            epsPlayer.settings.databaseEnabled = true;
            epsPlayer.settings.setDatabasePath(context.filesDir.absolutePath + "/databases");
            epsPlayer.settings.mediaPlaybackRequiresUserGesture = false;

            epsPlayer.addJavascriptInterface(
                MovieDetailFragment.WebAppInterface(context, vidEmbedURl), "Android")

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

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)

                    epsPlayer.loadUrl(
                        """javascript:(function f() {
                            this.interval = setInterval(() => {
                            document.querySelector('.jw-icon-fullscreen').addEventListener('click', function() {
                                document.querySelector('.jw-icon-fullscreen').style.background = 'Red';
                            });
                        }, 200);
                      })()""".trimIndent().trimMargin()
                    );

                    epsPlayer.visibility = View.VISIBLE
                }

            }


        }
    }
}