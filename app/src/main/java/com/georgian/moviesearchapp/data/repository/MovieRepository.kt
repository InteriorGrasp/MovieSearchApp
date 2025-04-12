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
    suspend fun addFavoriteMovie(movie: Movie, userId: String) {
        try {
            val favoriteRef = firestore
                .collection("favorites")
                .document(userId)
                .collection("movies")
                .document(movie.imdbID)

            favoriteRef.set(movie).await()
        } catch (e: Exception) {
            throw Exception("Error saving favorite: ${e.message}")
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
    suspend fun deleteFavoriteMovie(movie: Movie, userId: String) {
        try {
            firestore
                .collection("favorites")
                .document(userId)
                .collection("movies")
                .document(movie.imdbID)
                .delete()
                .await()
        } catch (e: Exception) {
            throw Exception("Error deleting favorite movie: ${e.message}")
        }
    }



    suspend fun getFavoriteMovies(userId: String): List<Movie> {
        return try {
            val snapshot = firestore
                .collection("favorites")
                .document(userId)
                .collection("movies")
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toObject(Movie::class.java) }
        } catch (e: Exception) {
            Log.e("MovieRepository", "Error fetching favorite movies: ${e.message}")
            emptyList()
        }
    }

    suspend fun updateFavoriteMovie(movie: Movie, userId: String) {
        val docRef = firestore
            .collection("favorites")
            .document(userId)
            .collection("movies")
            .document(movie.imdbID)

        docRef.set(movie).await()
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
