package com.anatame.pickaflix.presentation.Fragments.Search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anatame.pickaflix.common.Resource
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.MovieItem
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.SearchMovieItem
import com.anatame.pickaflix.data.remote.PageParser.Home.Parser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel constructor(
    private val parser: Parser = Parser()
) : ViewModel() {

    val searchList: MutableLiveData<Resource<List<SearchMovieItem>>> = MutableLiveData()

    fun getSearch(searchTerm: String){

        viewModelScope.launch(Dispatchers.IO){
            searchList.postValue(Resource.Loading())
            val response = parser.getSearchItem(searchTerm)
            searchList.postValue(Resource.Success(response))

        }

    }

}