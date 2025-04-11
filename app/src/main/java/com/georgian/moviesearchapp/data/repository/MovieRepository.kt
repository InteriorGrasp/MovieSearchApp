package com.georgian.moviesearchapp.data.repository

import com.georgian.moviesearchapp.data.network.ApiService
import com.georgian.moviesearchapp.data.model.Movie
import com.georgian.moviesearchapp.data.model.MovieDetail
import com.georgian.moviesearchapp.data.network.MovieResponse

class MovieRepository(private val apiService: ApiService) {

    // Store search results in MovieList model
    suspend fun searchMovies(query: String): List<Movie> {
        val response: MovieResponse = apiService.searchMovies(query)
        return response.search.filter { it.Title.contains(query, ignoreCase = true) }
    }

    // Fetch movie details and save in MovieDetail model
    suspend fun getMovieDetails(imdbID: String): MovieDetail {
        return apiService.getMovieDetails(imdbID)
    }
}


