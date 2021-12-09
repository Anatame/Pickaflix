package com.anatame.pickaflix.presentation.Fragments.Detail

import android.util.Log
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