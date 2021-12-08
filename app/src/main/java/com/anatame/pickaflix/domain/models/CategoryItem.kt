package com.anatame.pickaflix.domain.models

import androidx.lifecycle.MutableLiveData
import com.anatame.pickaflix.common.Resource
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.HeroItem
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.MovieItem

data class CategoryItem(
    val movieItemList: MutableLiveData<Resource<List<MovieItem>>>,
    val trendingShows: MutableLiveData<Resource<List<MovieItem>>>,
    val latestMovies: MutableLiveData<Resource<List<MovieItem>>>,
    val latestShows: MutableLiveData<Resource<List<MovieItem>>> = MutableLiveData(),
    val comingSoon: MutableLiveData<Resource<List<MovieItem>>> = MutableLiveData()
)
