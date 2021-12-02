package com.anatame.pickaflix.data.remote.PageParser.Home

import android.util.Log
import com.anatame.pickaflix.common.Constants.MOVIE_LIST_SELECTOR
import com.anatame.pickaflix.common.Constants.MOVIE_URL
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.MovieItem
import org.jsoup.Jsoup
import javax.inject.Inject

const val MOVIE_TAG = "movieItemList"

class Parser @Inject constructor() {
    var movieItemListData = ArrayList<MovieItem>()

    fun getMovieList(page: Int = 0): ArrayList<MovieItem> {
        val doc = Jsoup.connect(MOVIE_URL)
            .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
            .maxBodySize(0)
            .timeout(1000*5)
            .get()

        val movieItemList =  doc.getElementsByClass(MOVIE_LIST_SELECTOR)
        movieItemList.forEach { element ->
            val movieItem = element.allElements[0]

            val image = movieItem.select("img")
            val imageSrc = image.attr("data-src")

            val anchor = movieItem.select("a")
            val movieHref = anchor.attr("href")
            val movieTitle = anchor.attr("title")

            val movieFdiItem = movieItem.getElementsByClass("fdi-item")
            val movieReleaseDate = movieFdiItem[0]?.text()
            val movieLength = movieFdiItem[1]?.text()

            val movieQuality =  movieItem.getElementsByClass("pick film-poster-quality").text()
            val movieType = movieItem.getElementsByClass("fdi-type").text()

//            LogMovieItems(
//                movieTitle,
//                movieHref,
//                imageSrc,
//                movieReleaseDate,
//                movieLength,
//                movieQuality,
//                movieType
//            )

            val movieItemData = MovieItem(
                title = movieTitle,
                thumbnailUrl = imageSrc,
                Url = movieHref,
                releaseDate = movieReleaseDate!!,
                length = movieLength!!,
                quality = movieQuality,
                movieType = movieType
            )

                movieItemListData.add(movieItemData)
            }

        return movieItemListData

//            element.allElements.forEachIndexed { index, mElement ->
//               val item = mElement.getElementsByClass("film-poster")[index]
//                Log.d(MOVIE_TAG, item.select("img")[0].absUrl("src"))
//            }
        }

    private fun LogMovieItems(
        movieTitle: String?,
        movieHref: String?,
        imageSrc: String?,
        movieReleaseDate: String?,
        movieLength: String?,
        movieQuality: String?,
        movieType: String?
    ) {
        Log.d(
            MOVIE_TAG, """
                    Movie Title = $movieTitle
                    Movie Href = https://fmoviesto.cc$movieHref
                    Movie src = $imageSrc
                    Movie release date = $movieReleaseDate
                    Movie length = $movieLength
                    Movie quality = $movieQuality
                    Movie type = $movieType
                    
                    """.trimIndent()
        )
    }
}