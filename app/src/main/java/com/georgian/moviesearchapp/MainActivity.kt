package com.georgian.moviesearchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.georgian.moviesearchapp.data.network.ApiService
import com.georgian.moviesearchapp.data.repository.MovieRepository
import com.georgian.moviesearchapp.ui.navigation.MovieNavGraph
import com.georgian.moviesearchapp.ui.viewmodel.MovieViewModel
import com.georgian.moviesearchapp.ui.viewmodel.MovieViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Firebase.firestore
        auth = FirebaseAuth.getInstance()

        setContent {
            val navController = rememberNavController()

            // Set up API, repository, and view model
            val apiService = ApiService.create()
            val movieRepository = MovieRepository(apiService)
            val movieViewModel: MovieViewModel by viewModels {
                MovieViewModelFactory(movieRepository)
            }

            // Pass ViewModel to the navigation graph
            MovieNavGraph(
                navController = navController,
                movieViewModel = movieViewModel
            )
        }
    }

    override fun onStop() {
        super.onStop()
        auth.signOut()  // Sign out logic if needed
    }
}
