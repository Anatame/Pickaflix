package com.anatame.pickaflix.data.remote.PageParser.Home

import android.util.Log
import com.anatame.pickaflix.common.Constants.MOVIE_LIST_SELECTOR
import com.anatame.pickaflix.common.Constants.MOVIE_URL
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import javax.inject.Inject








const val MOVIE_TAG = "movieItemList"

class Parser @Inject constructor() {

    fun getSeasons(showDataID: String = "39495"){
        val seasonList = ArrayList<SeasonItem>()

        val doc = Jsoup.connect("https://fmoviesto.cc/ajax/v2/tv/seasons/$showDataID")
            .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
            .maxBodySize(0)
            .timeout(1000 * 5)
            .get()

        val dropDownItem = doc.getElementsByClass("dropdown-item")
        dropDownItem.forEach { item ->
            seasonList.add(
                SeasonItem(item.text(), item.attr("data-id"))
            )
        }

        Log.d("seasonData",
            """
            ${seasonList.toString()}
            ${getEpisodes(seasonList[0].seasonDataID)}
            
            """.trimIndent()
        )
    }

    fun getEpisodes(seasonID: String): ArrayList<EpisodeItem>{
        val episodeList = ArrayList<EpisodeItem>()

        val doc = Jsoup.connect("https://fmoviesto.cc/ajax/v2/season/episodes/$seasonID")
            .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
            .maxBodySize(0)
            .timeout(1000 * 5)
            .get()

        val episodeItems = doc.getElementsByClass("eps-item")
        episodeItems.forEach { item ->
            episodeList.add(
                EpisodeItem(item.attr("title"), item.attr("data-id"))
            )
        }

        return episodeList
    }

    fun getMovieDetails(movieName: String): MovieDetails {
        lateinit var movieDetail: MovieDetails

        getSeasons()

        val url = "https://fmoviesto.cc${movieName}"
        val doc = Jsoup.connect(url)
            .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
            .maxBodySize(0)
            .timeout(1000 * 5)
            .get()


        val movieItems = doc.getElementsByClass("page-detail")
        movieItems.forEach { element ->
            val movieItem = element.allElements[0]
            val headerContainer = movieItem.getElementsByClass("heading-name")
            val statsContainer = movieItem.getElementsByClass("stats")
            val statItems = statsContainer[0].getElementsByClass("mr-3")

            val movieTitle = headerContainer.select("a").text()
            val movieQuality = movieItem.getElementsByClass("btn-quality").text()
            val movieRating = statItems[1]?.text()
            val movieLength = statsContainer[0].allElements.last()?.text()
            val movieDescription = movieItem.getElementsByClass("description").text()
            val movieBackgroundCoverUrl =
                movieItem.getElementsByClass("w_b-cover").attr("style").getBackgroundImageUrl()
            var country = ""
            var genre = ""
            var released = ""
            var production = ""
            var casts = ""
            val movieDataID = movieItem.getElementsByClass("detail_page-watch").attr("data-id")
            val movieTrailer = doc.getElementById("iframe-trailer")
                ?.attr("data-src")
                .toString()

            val elements = movieItem.getElementsByClass("row-line")
            elements.forEach { item ->
                if (item.select("span").text() == "Country:") country = item.select("a").text()
                if (item.select("span").text() == "Genre:") {
                    item.select("a").forEach { g ->
                        if(genre.isNotEmpty()){
                            genre += ", ${g.text()}"
                        } else {
                            genre = g.text()
                        }
                    }
                }
                if (item.select("span").text() == "Released:") released = item.text()
                if (item.select("span").text() == "Production:") {
                    item.select("a").forEach { p ->
                        if(production.isNotEmpty()){
                            production += ", ${p.text()}"
                        } else {
                            production = p.text()
                        }

                    }
                }

                if (item.select("span").text() == "Casts:") {
                    item.select("a").forEach { c ->
                        if(casts.isNotEmpty()){
                            casts += ", ${c.text()}"
                        } else {
                            casts = c.text()
                        }
                    }
                }
            }

//                Log.d(
//                    "movieDetailsHtml", """
//                $movieTitle
//                $movieQuality
//                $movieRating
//                $movieLength
//                $movieDescription
//                $movieBackgroundCoverUrl
//
//                $country
//                $genre
//                $released
//                $production
//                $casts
//            """.trimIndent()
//                )

            movieDetail = MovieDetails(
                        movieTrailer,
                        movieDataID,
                        movieTitle,
                        movieQuality,
                        movieRating!!,
                        movieLength!!,
                        movieDescription,
                        movieBackgroundCoverUrl,
                        country,
                        genre,
                        released,
                        production,
                        casts
                )
            }

        return movieDetail
    }

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
            val backgroundImageUrl = sliderDivStyle.getBackgroundImageUrl()
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


fun String.getBackgroundImageUrl(): String{
    return this.substring(
        (this.indexOf('(') + 1),
        this.indexOf(')')
    )
}