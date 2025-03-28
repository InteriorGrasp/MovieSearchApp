package com.georgian.moviesearchapp.ui.viewmodel

//connects the user interface with the backend, ensuring that movie searches and
//details are fetched properly.
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.georgian.moviesearchapp.data.model.Movie
import com.georgian.moviesearchapp.data.model.MovieDetail
import com.georgian.moviesearchapp.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    // StateFlow for movie list
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    // StateFlow for movie details
    private val _movieDetails = MutableStateFlow<MovieDetail?>(null)
    val movieDetails: StateFlow<MovieDetail?> = _movieDetails

    // StateFlow to manage search state (loading, error, etc.)
    private val _searchState = MutableStateFlow(SearchState())
    val searchState: StateFlow<SearchState> = _searchState

    // StateFlow for movie details loading state
    private val _movieDetailState = MutableStateFlow(SearchState())

    // Function to search movies
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

    // Function to get movie details
    fun getMovieDetails(imdbID: String) {
        _movieDetailState.value = _movieDetailState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val response = movieRepository.getMovieDetails(imdbID)
                _movieDetails.value = response
                _movieDetailState.value = _movieDetailState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _movieDetailState.value = _movieDetailState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    // State to manage loading state and errors
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
            return MovieViewModel(movieRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
