package com.georgian.moviesearchapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.georgian.moviesearchapp.data.model.Movie
import com.georgian.moviesearchapp.data.model.MovieDetail
import com.georgian.moviesearchapp.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    // StateFlow for movie list
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()

    // StateFlow for favorite movies
    private val _favoriteMovies = MutableStateFlow<List<Movie>>(emptyList())
    val favoriteMovies: StateFlow<List<Movie>> = _favoriteMovies.asStateFlow()

    // StateFlow to manage search state (loading, error, etc.)
    private val _searchState = MutableStateFlow(SearchState())
    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()

    // StateFlow for loading state and errors related to adding/editing movies
    private val _movieState = MutableStateFlow(MovieState())
    val movieState: StateFlow<MovieState> = _movieState.asStateFlow()

    // StateFlow for movie details
    private val _movieDetails = MutableStateFlow<MovieDetail?>(null)
    val movieDetails: StateFlow<MovieDetail?> = _movieDetails


    init {
        loadFavoriteMovies()
    }

    private fun loadFavoriteMovies() {
        viewModelScope.launch {
            try {
                _favoriteMovies.value = movieRepository.getFavoriteMovies()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun getMovieDetails(movieId: String) {
        viewModelScope.launch {
            try {
                val movie = movieRepository.getMovieDetails(movieId)
                val movieDetail = MovieDetail(
                    title = movie.title,
                    year = movie.year,
                    rated = movie.rated,
                    released = movie.released,
                    runtime = movie.runtime,
                    genre = movie.genre,
                    director = movie.director,
                    writer = movie.writer,
                    actors = movie.actors,
                    plot = movie.plot,
                    language = movie.language,
                    country = movie.country,
                    awards = movie.awards,
                    poster = movie.poster,
                    imdbRating = movie.imdbRating,
                    imdbVotes = movie.imdbVotes,
                    imdbID = movie.imdbID,
                    type = movie.type,
                    boxOffice = movie.boxOffice,
                    website = movie.website,
                    production = movie.production,

                    )
                _movieDetails.value = movieDetail
            } catch (e: Exception) {
                _movieDetails.value = null
            }
        }
    }

    fun searchMovies(query: String) {
        _searchState.value = _searchState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val response = movieRepository.searchMovies(query)
                _movies.value = response
                _searchState.value = _searchState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _searchState.value = _searchState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun addFavoriteMovie(movie: Movie) {
        viewModelScope.launch {
            try {
                movieRepository.addMovie(movie)
                _favoriteMovies.value = _favoriteMovies.value + movie
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    // State to manage loading, error, and data for a single movie
    data class MovieState(
        val isLoading: Boolean = false,
        val movie: Movie? = null,
        val error: String? = null
    )

    // State to manage loading state and errors for searching
    data class SearchState(
        val isLoading: Boolean = false,
        val error: String? = null
    )
}

class MovieViewModelFactory(
    private val movieRepository: MovieRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieViewModel(movieRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}