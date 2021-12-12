package com.anatame.pickaflix.presentation.Fragments.Detail

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anatame.pickaflix.common.Resource
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.MovieDetails
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.MovieItem
import com.anatame.pickaflix.data.remote.PageParser.Home.Parser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDetailViewModel (
    private val parser: Parser = Parser()
        ) : ViewModel() {

    val movieDetails: MutableLiveData<Resource<MovieDetails>> = MutableLiveData()
    val vidEmbedLink: MutableLiveData<Resource<String>> = MutableLiveData()

    init{
        Log.d("movieDetailViewModel", "started")
    }

    fun getMovieData(url: String){
        val movieDataID: String = url.substring(url.lastIndexOf("-") + 1, (url.length))
        Log.d("movieSeasons","$url $movieDataID")
        viewModelScope.launch (Dispatchers.IO) {
            try {
                val servers = parser.getMovieServers(movieDataID)
                Log.d("movieSeasons", servers.toString())

                val vidSrc = parser.getVidSource(servers.first().serverDataId)
                Log.d("movieSeasons", vidSrc.toString())
                vidEmbedLink.postValue(Resource.Success(vidSrc.link))

            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun getSeasons(url: String){
        val showDataID: String = url.substring(url.lastIndexOf("-") + 1, (url.length))
        Log.d("movieSeasons","$url $showDataID")
        viewModelScope.launch (Dispatchers.IO) {
            try {
              val begin = System.currentTimeMillis()
//                movieDetails.postValue(Resource.Loading())
                val response = parser.getSeasons(showDataID)
//                movieDetails.postValue(Resource.Success(response))
                Log.d("movieSeasons", response.toString())

                val eps = parser.getEpisodes(response.first().seasonDataID)
                Log.d("movieSeasons", eps.toString())

                val servers = parser.getServers(eps.first().episodeDataID)
                Log.d("movieSeasons", servers.toString())

                val vidSrc = parser.getVidSource(servers.first().serverDataId)
                Log.d("movieSeasons", vidSrc.toString())
                vidEmbedLink.postValue(Resource.Success(vidSrc.link))

                Log.d("movieSeasons", (System.currentTimeMillis() - begin).toString())

            } catch (e: Exception){
                e.printStackTrace()
            }

        }
    }

    fun getVidSrc(movieSrc: String){
        viewModelScope.launch (Dispatchers.IO) {
            try {
                movieDetails.postValue(Resource.Loading())
                val response = parser.getMovieDetails(movieSrc)
                movieDetails.postValue(Resource.Success(response))
            } catch (e: Exception){
                e.printStackTrace()
            }

        }
    }

    fun getMovieDetails(movieSrc: String){
        viewModelScope.launch (Dispatchers.IO) {
            try {
                movieDetails.postValue(Resource.Loading())
                val response = parser.getMovieDetails(movieSrc)
                movieDetails.postValue(Resource.Success(response))
            } catch (e: Exception){
                e.printStackTrace()
            }

        }
    }
}