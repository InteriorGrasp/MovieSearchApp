package com.georgian.moviesearchapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetail(
    @SerialName("Title")
    val title: String? = null,

    @SerialName("Year")
    val year: String? = null,

    @SerialName("Rated")
    val rated: String? = null,

    @SerialName("Released")
    val released: String? = null,

    @SerialName("Runtime")
    val runtime: String? = null,

    @SerialName("Genre")
    val genre: String? = null,

    @SerialName("Director")
    val director: String? = null,

    @SerialName("Writer")
    val writer: String? = null,

    @SerialName("Actors")
    val actors: String? = null,

    @SerialName("Plot")
    val plot: String? = null,

    @SerialName("Language")
    val language: String? = null,

    @SerialName("Country")
    val country: String? = null,

    @SerialName("Awards")
    val awards: String? = null,

    @SerialName("Poster")
    val poster: String? = null,

    @SerialName("imdbRating")
    val imdbRating: String? = null,

    @SerialName("imdbVotes")
    val imdbVotes: String? = null,

    @SerialName("imdbID")
    val imdbID: String? = null,

    @SerialName("Type")
    val type: String? = null,

    @SerialName("BoxOffice")
    val boxOffice: String? = null,

    @SerialName("Production")
    val production: String? = null,

    @SerialName("Website")
    val website: String? = null,

    var id: String? = null // Firestore document ID
)


@Serializable
data class Rating(
    @SerialName("Source")
    val source: String,

    @SerialName("Value")
    val value: String
)
