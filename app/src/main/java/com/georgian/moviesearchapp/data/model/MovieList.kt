package com.georgian.moviesearchapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    @SerialName("Title")
    val title: String = "",

    @SerialName("Year")
    val year: String = "",

    @SerialName("imdbID")
    val imdbID: String = "",

    @SerialName("Type")
    val type: String = "",

    @SerialName("Poster")
    val poster: String = "",

    var id: String? = null // Add an 'id' property to hold the Firestore document ID
)
