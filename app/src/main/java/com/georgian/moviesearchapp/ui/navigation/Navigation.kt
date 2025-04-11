package com.georgian.moviesearchapp.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.georgian.moviesearchapp.ui.screens.*
import com.georgian.moviesearchapp.ui.viewmodel.MovieViewModel
import com.georgian.moviesearchapp.data.auth.FirebaseAuthManager

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Search : Screen("movie_search")
    object Detail : Screen("movie_detail/{imdbID}") {
        fun createRoute(imdbID: String) = "movie_detail/$imdbID"
    }
    object Favorites : Screen("favorites")
    object FavoriteEdit : Screen("favorite_edit/{movieId}") {
        fun createRoute(movieId: String) = "favorite_edit/$movieId"
    }
}

@Composable
fun MovieNavGraph(navController: NavHostController, movieViewModel: MovieViewModel) {
    val isLoggedIn = remember { mutableStateOf(FirebaseAuthManager.getCurrentUser() != null) }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn.value) Screen.Search.route else Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }

        composable(Screen.Register.route) {
            RegisterScreen(navController = navController)
        }

        composable(Screen.Search.route) {
            MovieSearchScreen(navController = navController, movieViewModel = movieViewModel)
        }

        // ✅ Now the Detail screen only receives imdbID and lets the screen handle the rest
        composable(Screen.Detail.route) { backStackEntry ->
            val imdbID = backStackEntry.arguments?.getString("imdbID") ?: return@composable
            MovieDetailScreen(
                imdbID = imdbID,
                navController = navController,
                movieViewModel = movieViewModel
            )
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(navController = navController, movieViewModel = movieViewModel)
        }

        composable(Screen.FavoriteEdit.route) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")
            movieId?.let {
                FavoriteEditScreen(movieId = it, navController = navController, movieViewModel = movieViewModel)
            }
        }
    }
}
