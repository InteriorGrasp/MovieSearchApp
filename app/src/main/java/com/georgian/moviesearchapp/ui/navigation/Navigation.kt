package com.georgian.moviesearchapp.ui.navigation
 // It controls which screen the user sees when they search for movies
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.georgian.moviesearchapp.ui.screens.MovieDetailScreen
import com.georgian.moviesearchapp.ui.screens.MovieSearchScreen
import com.georgian.moviesearchapp.ui.viewmodel.MovieViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp

sealed class Screen(val route: String) {
    object Search : Screen("movie_search")
    object Detail : Screen("movie_detail/{movieId}") {
        fun createRoute(movieId: String) = "movie_detail/$movieId"
    }
}

@Composable
fun MovieNavGraph(navController: NavHostController, movieViewModel: MovieViewModel) {

    NavHost(navController = navController, startDestination = Screen.Search.route) {
        composable(Screen.Search.route) {
            MovieSearchScreen(navController = navController, viewModel = movieViewModel)
        }

        composable(Screen.Detail.route) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")
            var errorMessage by remember { mutableStateOf<String?>(null) }

            movieId?.let {
                try {
                    movieViewModel.getMovieDetails(it)
                } catch (e: Exception) {
                    errorMessage = "Movie not found. Please try another search."
                }
            }

            val movieDetail by movieViewModel.movieDetails.collectAsState()

            Box(modifier = Modifier.fillMaxSize()) {
                // Loading State
                AnimatedVisibility(visible = movieDetail == null && errorMessage == null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(50.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 4.dp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Fetching movie details...",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                // Error State
                AnimatedVisibility(visible = errorMessage != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = errorMessage ?: "Unknown error occurred",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { navController.popBackStack() }) {
                            Text("Go Back to Search")
                        }
                    }
                }

                // Show Movie Details if Found
                movieDetail?.let {
                    MovieDetailScreen(movie = it, navController = navController)
                }
            }
        }
    }
}
