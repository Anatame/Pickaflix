package com.anatame.pickaflix.data.remote.PageParser.Home.DTO

import java.io.Serializable

data class MovieItem(
    val title: String,
    val thumbnailUrl: String,
    val Url: String,
    val releaseDate: String,
    val quality: String,
    val length: String,
    val movieType: String,

) : Serializable
