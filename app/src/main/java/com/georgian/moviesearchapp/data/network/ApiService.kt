package com.georgian.moviesearchapp.data.network

import com.georgian.moviesearchapp.data.model.Movie
import com.georgian.moviesearchapp.data.model.MovieDetail
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.Json

interface ApiService {
    suspend fun searchMovies(query: String): MovieResponse
    suspend fun getMovieDetails(imdbID: String): MovieDetail

    companion object {
        private const val BASE_URL = "https://www.omdbapi.com/"
        private const val API_KEY = "683a2222"

        fun create(): ApiService {
            val client = HttpClient {
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                    })
                }
            }

            return object : ApiService {
                override suspend fun searchMovies(query: String): MovieResponse {
                    return client.get(BASE_URL) {
                        parameter("apikey", API_KEY)
                        parameter("s", query)
                        parameter("type", "movie")
                    }.body()
                }

                override suspend fun getMovieDetails(imdbID: String): MovieDetail {
                    return client.get(BASE_URL) {
                        parameter("apikey", API_KEY)
                        parameter("i", imdbID)
                        parameter("plot", "full")
                    }.body()
                }
            }
        }
    }
}

@Serializable
data class MovieResponse(
    @SerialName("Search")  // The key "Search" in the API response
    val search: List<Movie> = emptyList(),

    @SerialName("totalResults")
    val totalResults: String = "",

    @SerialName("Response")
    val response: String = ""
)