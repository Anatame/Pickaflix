package com.anatame.pickaflix.presentation.Fragments.home


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anatame.pickaflix.common.Resource
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.MovieItem
import com.anatame.pickaflix.data.remote.PageParser.Home.Parser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val parser: Parser
) : ViewModel() {
    val Movies: MutableLiveData<Resource<List<MovieItem>>> = MutableLiveData()

    init{
        getHomeScreenData()
      //  getSliderItems()
      //  getSearch()
       // getOkHttpSearchResult()
    }

//    fun getOkHttpSearchResult(){
//        viewModelScope.launch(Dispatchers.IO){
//            parser.getHttpSearchItem()
//        }
//    }

    // this is in search view model now
//    fun getSearch(){
//        viewModelScope.launch(Dispatchers.IO){
//            parser.getSearchItem()
//        }
//    }
//
//    fun getSliderItems(){
//        viewModelScope.launch (Dispatchers.IO)  {
//            parser.getHeroSectionItems()
//        }
//    }

    fun getHomeScreenData(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Movies.postValue(Resource.Loading())
                val response = parser.getMovieList()
                Movies.postValue(Resource.Success(response))
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

}