package com.georgian.moviesearchapp.data.model

import kotlinx.serialization.Serializable

//list of searched movie
@Serializable
data class Movie(
    val Title: String,
    val Year: String,
    val imdbID: String,
    val Type: String,
    val Poster: String
)
