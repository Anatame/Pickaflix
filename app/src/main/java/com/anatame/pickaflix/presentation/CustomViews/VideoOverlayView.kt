package com.anatame.pickaflix.presentation.CustomViews

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.ImageButton
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anatame.pickaflix.R
import com.anatame.pickaflix.presentation.Adapters.DetailsRVKingAdapter
import com.anatame.pickaflix.presentation.Adapters.DetailsRVMasterAdapter
import androidx.core.view.MotionEventCompat
import com.anatame.pickaflix.common.utils.PlayerHelper
import com.anatame.pickaflix.databinding.VideoOverlayFullscreenBinding
import com.anatame.pickaflix.databinding.VideoOverlaySmallBinding
import com.google.android.exoplayer2.ui.PlayerView


class VideoOverlayView
@JvmOverloads constructor(
     context: Context,
     attrs: AttributeSet? = null,
     defStyleAttr: Int = 0) : MotionLayout(context, attrs, defStyleAttr) {

    private var binding: VideoOverlayFullscreenBinding
    private var motionLayout: MotionLayout
    private val touchableArea: View

    private lateinit var playerHelper: PlayerHelper
    private val playIcon = R.drawable.ic_baseline_play_arrow_24
    private val pauseIcon = R.drawable.ic_baseline_pause_24

    private val clickableArea: View
    private val playerView: PlayerView
    private val tvTitle: View
    private val playBtn: ImageButton
    private var rv: RecyclerView

    private var startX: Float? = null
    private var startY: Float? = null

    init {
        binding = VideoOverlayFullscreenBinding.inflate(LayoutInflater.from(context), this, false)
        addView(binding.root)
        motionLayout = binding.container

        touchableArea = binding.videoOverlayTouchableArea
        clickableArea = binding.videoOverlayThumbnail
        playerView = binding.actPlayer
        tvTitle = binding.videoOverlayTitle
        playBtn = binding.playBtn

        rv = binding.rvDetails
        initRecyclerview()

    }

    private fun initRecyclerview(){
        if (rv != null) {
            Log.d("nigga","the recyclerview is hereee")
            rv.layoutManager = LinearLayoutManager(context)
            rv.adapter = DetailsRVKingAdapter(context, rv)

        }

    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val isInProgress = (motionLayout.progress > 0.0f && motionLayout.progress < 1.0f)
        val isInTarget = touchEventInsideTargetView(touchableArea, ev)

        return if (isInProgress || isInTarget ) {
            super.onInterceptTouchEvent(ev)
        } else {
            true
        }

        super.onInterceptTouchEvent(ev)
    }

    private fun touchEventInsideTargetView(v: View, ev: MotionEvent): Boolean {
        if (ev.x > v.left && ev.x < v.right) {
            if (ev.y > v.top && ev.y < v.bottom) {
                return true
            }
        }
        return false
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {

//        if (touchEventInsideTargetView(clickableArea, ev)) {
//            when (ev.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    startX = ev.x
//                    startY = ev.y
//                }
//
//                MotionEvent.ACTION_UP   -> {
//                    val endX = ev.x
//                    val endY = ev.y
//                    if(startX != null && startY!= null){
//                        if (isAClick(startX!!, endX, startY!!, endY)) {
//                            if (doClickTransition()) {
//                                return true
//                            }
//                        }
//                    }
//                }
//            }
//        }



        if (touchEventInsideTargetView(playBtn, ev)) {
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = ev.x
                    startY = ev.y
                }

                MotionEvent.ACTION_UP   -> {
                    val endX = ev.x
                    val endY = ev.y
                    if(startX != null && startY!= null){
                        if (isAClick(startX!!, endX, startY!!, endY)) {
                            handlePlayerControls()
                        }
                    }
                }
            }
        }

        if (touchEventInsideTargetView(tvTitle, ev)) {
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = ev.x
                    startY = ev.y
                }

                MotionEvent.ACTION_UP   -> {
                    val endX = ev.x
                    val endY = ev.y
                    if(startX != null && startY!= null) {
                        if (isAClick(startX!!, endX, startY!!, endY)) {
                            Toast.makeText(context, "brah", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    private fun doClickTransition(): Boolean {
        var isClickHandled = false
        if (motionLayout.progress < 0.05F) {
            motionLayout.transitionToEnd()
            isClickHandled = true
        } else if (motionLayout.progress > 0.95F) {
            motionLayout.transitionToStart()
            isClickHandled = true
        }
        return isClickHandled
    }

    private fun isAClick(startX: Float, endX: Float, startY: Float, endY: Float): Boolean {
        val differenceX = Math.abs(startX - endX)
        val differenceY = Math.abs(startY - endY)
        return !/* =5 */(differenceX > 200 || differenceY > 200)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return false
    }

    fun loadView(){
        motionLayout.transitionToEnd()
    }

    fun loadPlayer(){
        val hlrUrl =   "https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8"
        playerHelper = PlayerHelper(context, playerView,  hlrUrl)
        playerHelper.initPlayer()
    }

    private fun handlePlayerControls() {
        if (playerHelper.isPlaying()) {
            playerHelper.pause()
            playBtn.setImageDrawable(
                ContextCompat.getDrawable(context, playIcon)
            );

        } else {
            playerHelper.play()
            playBtn.setImageDrawable(
                ContextCompat.getDrawable(context, pauseIcon)
            );
        }
    }



}
