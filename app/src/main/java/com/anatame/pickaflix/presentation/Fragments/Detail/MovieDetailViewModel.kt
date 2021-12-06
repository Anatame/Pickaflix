package com.anatame.pickaflix.presentation.Fragments.Detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anatame.pickaflix.data.remote.PageParser.Home.Parser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDetailViewModel (
    private val parser: Parser = Parser()
        ) : ViewModel() {

    fun getMovieDetails(movieSrc: String){
        viewModelScope.launch (Dispatchers.IO) {
            val response = parser.getMovieDetails(movieSrc)
            Log.d("movieDetailViewModel", response.toString())
        }
    }
}