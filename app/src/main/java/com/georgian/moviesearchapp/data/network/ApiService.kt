package com.georgian.moviesearchapp.data.network

import com.georgian.moviesearchapp.data.model.Movie
import com.georgian.moviesearchapp.data.model.MovieDetail
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable

interface ApiService {

    suspend fun searchMovies(query: String): MovieResponse
    suspend fun getMovieDetails(imdbID: String): MovieDetail

    companion object {
        private const val BASE_URL = "https://www.omdbapi.com/"
        private const val API_KEY = "683a2222"

        fun create(): ApiService {
            // Initialize HttpClient
            val client = HttpClient {
                install(ContentNegotiation) {
                    json(Json { prettyPrint = true; isLenient = true })
                }
            }

            return object : ApiService {

                // Fetch movie search results
                override suspend fun searchMovies(query: String): MovieResponse {
                    val response = client.get(BASE_URL) {
                        parameter("apikey", API_KEY)
                        parameter("s", query)
                        parameter("type", "movie")
                    }
                    return response.body() // Deserialize the response to MovieResponse
                }

                // Fetch movie details by IMDb ID
                override suspend fun getMovieDetails(imdbID: String): MovieDetail {
                    val response = client.get(BASE_URL) {
                        parameter("apikey", API_KEY)
                        parameter("i", imdbID)
                    }
                    return response.body() // Deserialize the response to MovieDetail
                }
            }
        }
    }
}

@Serializable
data class MovieResponse(
    val Search: List<Movie>,
    val totalResults: String,
    val Response: String
) {
    // Utility function to check if any movie title matches the search query
    fun contains(query: String): Boolean {
        return Search.any {
            it.Title.contains(query, ignoreCase = true)
        }
    }
}
