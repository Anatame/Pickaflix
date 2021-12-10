package com.anatame.pickaflix.data.remote.retrofit

data class VidData(
    val link: String,
    val sources: List<Any>,
    val title: String,
    val tracks: List<Any>,
    val type: String
)