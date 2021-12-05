package com.anatame.pickaflix.data.remote.PageParser.Home

import android.util.Log
import com.anatame.pickaflix.common.Constants.MOVIE_LIST_SELECTOR
import com.anatame.pickaflix.common.Constants.MOVIE_URL
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.MovieItem
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.SearchMovieItem
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Connection
import org.jsoup.Jsoup
import javax.inject.Inject








const val MOVIE_TAG = "movieItemList"

class Parser @Inject constructor() {

    fun getSearchItem(searchTerm: String): ArrayList<SearchMovieItem> {
        val searchItemList = ArrayList<SearchMovieItem>()

        val client = OkHttpClient()
        val formBody= FormBody.Builder()
            .addEncoded("keyword", "$searchTerm")
            .build()

        val request = Request.Builder()
            .url("https://fmoviesto.cc/ajax/search")
            .addHeader("authority", "fmoviesto.cc")
            .addHeader("accept", "*/*")
            .addHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
            .addHeader("origin", "https://fmoviesto.cc")
            .addHeader("referer", "https://fmoviesto.cc/search/")
            .addHeader("sec-ch-ua-mobile", "?0")
            .addHeader("sec-ch-ua-platform", "Windows")
            .addHeader("sec-fetch-mode", "cors")
            .addHeader("sec-fetch-site", "same-origin")
            .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36")
            .addHeader("x-requested-with", "XMLHttpRequest")

            .post(formBody)
            .build()

        // Execute request
        val response = client.newCall(request).execute()
        val result = response.body?.string()
        Log.d("okResponse", result.toString())
        Log.d("okResponse", searchTerm)

        client.cache?.delete()

        val doc = Jsoup.parse(result!!)


        val movies = doc.select("a")
        movies.forEach { item ->
            val movieSrc = item.select("a").attr("href")
            val moviePoster = item.select("img").attr("src")
            val movieName = item.getElementsByClass("film-name").text()

            Log.d("searchReturn", """
               MovieSource = $movieSrc
               MoviePoster = $moviePoster
               MovieName = $movieName
            """.trimIndent())

            if(moviePoster.isNotEmpty()){
                searchItemList.add(SearchMovieItem(
                    thumbnailSrc = moviePoster,
                    title = movieName,
                    src = movieSrc
                ))
            }
        }

        return searchItemList

    }

    fun getHeroSectionItems(){
        val doc = Jsoup.connect(MOVIE_URL)
            .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
            .maxBodySize(0)
            .timeout(1000*5)
            .get()

        val sliderList = doc.getElementsByClass("swiper-slide")
        sliderList.forEach { element ->
            val sliderItem = element.allElements[0]

            val sliderDivStyle = sliderItem.attr("style")
            val backgroundImageUrl = sliderDivStyle.substring(
                (sliderDivStyle.indexOf('(') + 1),
                sliderDivStyle.indexOf(')')
            )

            val anchor = sliderItem.select("a")
            val movieHref = anchor.attr("href")
            val movieTitle = anchor.attr("title")

            val movieCaption = sliderItem.getElementsByClass("scd-item")[0]

            Log.d(MOVIE_TAG, """
                backgroundImage: $backgroundImageUrl
                title: $movieTitle
                href: $movieHref
                movieCaption: $movieCaption
            """.trimIndent())
        }
    }


    fun getMovieList(page: Int = 0): ArrayList<MovieItem> {
        var movieItemListData = ArrayList<MovieItem>()
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
            val movieLength = movieItem.getElementsByClass("fdi-duration").text()

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
                length = movieLength,
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
    fun getHttpSearchItem() {
        var client = OkHttpClient()
        val formBody= FormBody.Builder()
            .add("keyword", "Venom")
            .build()

        var request = Request.Builder()
            .url("https://fmoviesto.cc/ajax/search")
            .post(formBody)
            .build()

        // Execute request
        val response = client.newCall(request).execute()
        val result = response.body?.string()
        Log.d("okResponse", result.toString())


        val doc = Jsoup.parse(result!!)

        val movies = doc.select("a")
        movies.forEach { item ->
            val movieSrc = item.select("a").attr("href")
            val moviePoster = item.select("img").attr("src")
            val movieName = item.getElementsByClass("film-name").text()

            Log.d(
                "searchReturn", """
               MovieSource = $movieSrc
               MoviePoster = $moviePoster
               MovieName = $movieName
            """.trimIndent()
            )

        }
    }
}