package com.anatame.pickaflix.data.remote.PageParser.Home.DTO

import com.anatame.pickaflix.data.remote.PageParser.Home.getBackgroundImageUrl

data class MovieDetails(
    val movieDataID: String,
    val movieTitle : String,
    val movieQuality: String,
    val movieRating: String,
    val movieLength : String,
    val movieDescription : String,
    val movieBackgroundCoverUrl : String,
    var country: String,
    var genre: String,
    var released: String,
    var production: String,
    var casts: String,
)

