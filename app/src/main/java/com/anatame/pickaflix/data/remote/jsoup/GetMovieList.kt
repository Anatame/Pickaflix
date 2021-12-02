package com.anatame.pickaflix.data.remote.jsoup

import android.util.Log
import com.anatame.pickaflix.common.Constants.MOVIE_LIST_SELECTOR
import com.anatame.pickaflix.common.Constants.MOVIE_URL
import org.jsoup.Jsoup
import javax.inject.Inject

const val MOVIE_TAG = "movieItemList"

class GetMovieList @Inject constructor() {
    fun get(){
        val doc = Jsoup.connect(MOVIE_URL)
            .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
            .maxBodySize(0)
            .timeout(1000*5)
            .get()

        val movieItemList =  doc.getElementsByClass(MOVIE_LIST_SELECTOR)
        movieItemList.forEach { element ->

            Log.d(MOVIE_TAG, element.allElements.toString())
        }
    }
}