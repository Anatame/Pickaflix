package com.anatame.pickaflix.presentation.Fragments.home


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anatame.pickaflix.common.Resource
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.HeroItem
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.MovieItem
import com.anatame.pickaflix.data.remote.PageParser.Home.Parser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val parser: Parser
) : ViewModel() {
    val sliderItems: MutableLiveData<Resource<List<HeroItem>>> = MutableLiveData()
    val movies: MutableLiveData<Resource<List<MovieItem>>> = MutableLiveData()
    val trendingShows: MutableLiveData<Resource<List<MovieItem>>> = MutableLiveData()
    val latestMovies: MutableLiveData<Resource<List<MovieItem>>> = MutableLiveData()
    val latestShows: MutableLiveData<Resource<List<MovieItem>>> = MutableLiveData()
    val comingSoon: MutableLiveData<Resource<List<MovieItem>>> = MutableLiveData()

    val scrollState4 = MutableLiveData(0)

    init{
        getHomeScreenData()
       Log.d("viewModelInitialized", "viewModelInitialized")
        getSliderItems()
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
    fun getSliderItems(){
        viewModelScope.launch (Dispatchers.IO)  {
            try {
                sliderItems.postValue(Resource.Loading())
                val response =  parser.getHeroSectionItems()
                sliderItems.postValue(Resource.Success(response))
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun getHomeScreenData(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                movies.postValue(Resource.Loading())

                val response = parser.getMovieList()

                val trendingMovies: ArrayList<MovieItem> = ArrayList()
                response.forEachIndexed{index, item ->
                    if(index in 0..23){
                        trendingMovies.add(item)
                    }
                }

                val trendingShowItems: ArrayList<MovieItem> = ArrayList()
                response.forEachIndexed{index, item ->
                    if(index in 24..47){
                        trendingShowItems.add(item)
                    }
                }

                val latestMovieItems: ArrayList<MovieItem> = ArrayList()
                response.forEachIndexed{index, item ->
                    if(index in 48..71){
                        latestMovieItems.add(item)
                    }
                }

                val latestShowItems: ArrayList<MovieItem> = ArrayList()
                response.forEachIndexed{index, item ->
                    if(index in 72..95){
                        latestShowItems.add(item)
                    }
                }

                val comingSoonItems: ArrayList<MovieItem> = ArrayList()
                response.forEachIndexed{index, item ->
                    if(index in 96..120){
                        comingSoonItems.add(item)
                    }
                }



                movies.postValue(Resource.Success(trendingMovies))
                trendingShows.postValue(Resource.Success(trendingShowItems))
                latestMovies.postValue(Resource.Success(latestMovieItems))
                latestShows.postValue(Resource.Success(latestShowItems))
                comingSoon.postValue(Resource.Success(comingSoonItems))

            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

}