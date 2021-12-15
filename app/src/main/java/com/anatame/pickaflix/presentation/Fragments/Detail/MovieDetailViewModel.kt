package com.anatame.pickaflix.presentation.Fragments.Detail

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anatame.pickaflix.common.Resource
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.*
import com.anatame.pickaflix.data.remote.PageParser.Home.Parser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDetailViewModel (
    private val parser: Parser = Parser()
        ) : ViewModel() {

    val movieDetails: MutableLiveData<Resource<MovieDetails>> = MutableLiveData()
    val vidEmbedLink: MutableLiveData<Resource<String>> = MutableLiveData()

    val serverList: MutableLiveData<Resource<List<ServerItem>>> = MutableLiveData()
    val seasonList: MutableLiveData<Resource<List<SeasonItem>>> = MutableLiveData()
    val episodeList: MutableLiveData<Resource<List<EpisodeItem>>> = MutableLiveData()

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

                serverList.postValue(Resource.Success(servers))

                val vidSrc = parser.getVidSource(servers.first().serverDataId)
                Log.d("movieSeasons", vidSrc.toString())
                vidEmbedLink.postValue(Resource.Success(vidSrc.link))

            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun getEpisodes(seasonDataID: String){
        val eps = parser.getEpisodes(seasonDataID)
        episodeList.postValue(Resource.Success(eps))
    }

    fun getVideoSrc(serverDataId: String){
        viewModelScope.launch (Dispatchers.IO){
            val vidSrc = parser.getVidSource(serverDataId)
            Log.d("movieSeasons", vidSrc.toString())
            vidEmbedLink.postValue(Resource.Success(vidSrc.link))
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
                seasonList.postValue(Resource.Success(response))

                val eps = parser.getEpisodes(response.first().seasonDataID)
                episodeList.postValue(Resource.Success(eps))
                Log.d("movieSeasons", eps.toString())

                val servers = parser.getServers(eps.first().episodeDataID)
                serverList.postValue(Resource.Success(servers))
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

//    fun getVidSrc(movieSrc: String){
//        viewModelScope.launch (Dispatchers.IO) {
//            try {
//                movieDetails.postValue(Resource.Loading())
//                val response = parser.getMovieDetails(movieSrc)
//                movieDetails.postValue(Resource.Success(response))
//            } catch (e: Exception){
//                e.printStackTrace()
//            }
//
//        }
//    }

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