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
        private const val API_KEY = "683a2222" // Store in a secure place for production apps

        fun create(): ApiService {
            val client = HttpClient {
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    })
                }
            }

            return object : ApiService {

                override suspend fun searchMovies(query: String): MovieResponse {
                    val response = client.get(BASE_URL) {
                        parameter("apikey", API_KEY)
                        parameter("s", query)
                        parameter("type", "movie")
                    }
                    return response.body()
                }

                override suspend fun getMovieDetails(imdbID: String): MovieDetail {
                    val response = client.get(BASE_URL) {
                        parameter("apikey", API_KEY)
                        parameter("i", imdbID)
                    }
                    return response.body()
                }
            }
        }
    }
}

@Serializable
data class MovieResponse(
    @SerialName("Search")
    val search: List<Movie> = emptyList(),

    @SerialName("totalResults")
    val totalResults: String = "",

    @SerialName("Response")
    val response: String = ""
) {
    fun contains(query: String): Boolean {
        return search.any {
            it.Title.contains(query, ignoreCase = true)
        }
    }
}