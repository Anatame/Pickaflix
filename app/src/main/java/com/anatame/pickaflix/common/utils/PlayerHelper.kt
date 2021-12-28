package com.anatame.pickaflix.common.utils

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.text.ExoplayerCuesDecoder
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource

class PlayerHelper(
    private val context: Context,
    private val playerView: PlayerView,
    private val contentUrl: String
) {
    private var player: ExoPlayer? = null

    fun initPlayer(){
        playerView.useController = false
        player = ExoPlayer.Builder(context).build()
        playerView.player = player
        createMediaSource()
        if(player != null){
            player!!.setMediaSource(createMediaSource())
            player!!.prepare()
            player!!.playWhenReady = true
        }

    }

    private fun createMediaSource(): MediaSource {
        val dataSourceFactory = DefaultHttpDataSource.Factory()

        return HlsMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(contentUrl))
    }

    fun play(){
        player?.play()
    }

    fun pause(){
        player?.pause()
    }

    fun isPlaying(): Boolean{
        if (player != null) {
            return player!!.isPlaying
        } else {
            return false
        }
    }

    fun releasePlayer() {
        if (player == null) {
            return
        }
        player?.release()
        player = null
    }

}