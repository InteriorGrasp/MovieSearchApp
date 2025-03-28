package com.georgian.moviesearchapp
//implementation file where everything starts
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.georgian.moviesearchapp.ui.navigation.MovieNavGraph
import com.georgian.moviesearchapp.ui.viewmodel.MovieViewModel
import com.georgian.moviesearchapp.data.network.ApiService
import com.georgian.moviesearchapp.data.repository.MovieRepository
import com.georgian.moviesearchapp.ui.viewmodel.MovieViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Initialize navigation controller
            val navController = rememberNavController()

            // Initialize ApiService and MovieRepository
            val apiService = ApiService.create()
            val movieRepository = MovieRepository(apiService)

            // Initialize the ViewModel using viewModels delegate (prevents recreation on recomposition)
            val movieViewModel: MovieViewModel by viewModels {
                MovieViewModelFactory(movieRepository)
            }

            // Pass the ViewModel to the Navigation Graph
            MovieNavGraph(
                navController = navController,
                movieViewModel = movieViewModel
            )
        }
    }
}