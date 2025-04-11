package com.georgian.moviesearchapp.data.model


import kotlinx.serialization.Serializable

// movie details of selected one stored in variables
@Serializable
data class MovieDetail(
    val Title: String,
    val Year: String?,
    val Rated: String?,
    val Released: String?,
    val Runtime: String?,
    val Genre: String?,
    val Director: String?,
    val Writer: String?,
    val Actors: String?,
    val Plot: String?,
    val Language: String?,
    val Country: String?,
    val Awards: String?,
    val Poster: String?,
    val imdbRating: String?,
    val imdbVotes: String?,
    val imdbID: String,
    val BoxOffice: String?,
    val Website: String?
)


@Serializable
data class Rating(
    val Source: String,
    val Value: String
)




