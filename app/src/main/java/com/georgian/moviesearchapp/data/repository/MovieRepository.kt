package com.georgian.moviesearchapp.data.repository

import android.util.Log
import com.georgian.moviesearchapp.data.network.ApiService
import com.georgian.moviesearchapp.data.model.Movie
import com.georgian.moviesearchapp.data.model.MovieDetail
import com.georgian.moviesearchapp.data.network.MovieResponse
import com.georgian.moviesearchapp.ui.viewmodel.MovieViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class MovieRepository(private val apiService: ApiService) {

    private val firestore = FirebaseFirestore.getInstance()

    // Store search results in MovieList model
    suspend fun searchMovies(query: String): List<Movie> {
        try {
            val response: MovieResponse = apiService.searchMovies(query)
            return response.search.filter { it.title.contains(query, ignoreCase = true) }
        } catch (e: Exception) {
            throw Exception("Error fetching movie search results: ${e.message}")
        }
    }

    // Fetch movie details and save in MovieDetail model
    suspend fun getMovieDetails(imdbID: String): MovieDetail {
        try {
            return apiService.getMovieDetails(imdbID)
        } catch (e: Exception) {
            throw Exception("Error fetching movie details: ${e.message}")
        }
    }

    // Add a movie to Firestore
    suspend fun addMovie(movie: Movie) {
        try {
            val movieCollection = firestore.collection("movies")
            // Adds a new movie document to the collection. Firestore auto-generates the ID.
            Log.d("MovieRepository", "Before adding: ${movie.imdbID}")
            val documentRef = movieCollection.add(movie).await()
            movie.id = documentRef.id // Now we can assign the generated ID to the movie's id
            Log.d("MovieRepository", "Before adding: ${movie.imdbID}")
        } catch (e: Exception) {
            throw Exception("Error adding movie to Firestore: ${e.message}")
        }
    }

    // Update an existing movie in Firestore
    suspend fun updateMovie(movie: Movie) {
        try {
            // Ensure that the movie has an ID before trying to update
            if (movie.imdbID.isNullOrEmpty()) {
                throw Exception("Movie ID is required for update.")
            }
            val movieCollection = firestore.collection("movies")
            val movieDocRef = movieCollection.document(movie.imdbID) // Use movie.id as the document reference
            movieDocRef.set(movie).await() // Updates the movie document
        } catch (e: Exception) {
            throw Exception("Error updating movie in Firestore: ${e.message}")
        }
    }

    // Delete a movie from Firestore
    suspend fun deleteMovie(movieId: String) {
        try {
            if (movieId.isEmpty()) {
                throw Exception("Movie ID is required for deletion.")
            }
            val movieCollection = firestore.collection("movies")
            val movieDocRef = movieCollection.document(movieId) // Use movieId to get the document reference
            movieDocRef.delete().await() // Deletes the movie document
        } catch (e: Exception) {
            throw Exception("Error deleting movie from Firestore: ${e.message}")
        }
    }

    suspend fun getFavoriteMovies(): List<Movie> {
        val snapshot = firestore.collection("favorites").get().await()
        return snapshot.documents.mapNotNull { it.toObject(Movie::class.java) }
    }


    // Get a movie by ID from Firestore (for editing purposes)
    suspend fun getMovieById(movieId: String): MovieDetail? {
        try {
            if (movieId.isEmpty()) {
                throw Exception("Movie ID is required to fetch movie details.")
            }
            val movieCollection = firestore.collection("movies")
            val doc = movieCollection.document(movieId).get().await() // Fetches the document by ID
            return if (doc.exists()) {
                doc.toObject(MovieDetail::class.java) // Converts the document to the Movie object
            } else {
                null // Return null if the document does not exist
            }
        } catch (e: Exception) {
            throw Exception("Error fetching movie by ID: ${e.message}")
        }
    }
}
