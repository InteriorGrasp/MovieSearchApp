package com.georgian.moviesearchapp.data.network

import com.georgian.moviesearchapp.data.model.Movie
import com.georgian.moviesearchapp.data.model.MovieDetail
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//Api service to retrieve data from database of API
interface ApiService {
    @GET("?apikey=683a2222&type=movie")
    suspend fun searchMovies(@Query("s") query: String): MovieResponse //works with keyword search

    // Add method for fetching movie details using imdbID
    @GET("?apikey=683a2222")
    suspend fun getMovieDetails(@Query("i") imdbID: String): MovieDetail

    companion object {
        private const val BASE_URL = "https://www.omdbapi.com/"
        // Create Retrofit instance
        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}

//keyword search in db
data class MovieResponse(
    val Search: List<Movie>,
    val totalResults: String,
    val Response: String ) {
    // The 'contains' method checks if any movie in the 'Search' list contains the query string (case-insensitive).
    fun contains(query: String): Boolean {
    // Search for movies that have a title matching the query (case insensitive)
        return Search.any {
            it.Title.contains(query, ignoreCase = true)
        }
    }
}

