package com.anatame.pickaflix.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anatame.pickaflix.R
import com.anatame.pickaflix.common.utils.PlayerHelper
import com.anatame.pickaflix.databinding.ActivityNativePlayerBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util

class NativePlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNativePlayerBinding

    private lateinit var playerView: PlayerView
    private lateinit var player: ExoPlayer
    private lateinit var mediaSource: MediaSource

    private var contentUrl: String = "https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNativePlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val extras = intent.extras
//        if (extras != null) {
//            val url = extras.getString("vidUrl")
//            if (url != null) {
//                contentUrl = url
//            }
//        }

        Log.d("nativeURl", contentUrl)
        playerView = binding.playerView
        PlayerHelper(this, binding.playerView, contentUrl).initPlayer()
    }

}