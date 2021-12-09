package com.anatame.pickaflix.data.remote.PageParser.Home.DTO

import java.io.Serializable
import java.time.Duration

data class HeroItem(
    val backgroundImageUrl: String,
    val title: String,
    val source: String,
    val duration: String,
    val rating: String
): Serializable
