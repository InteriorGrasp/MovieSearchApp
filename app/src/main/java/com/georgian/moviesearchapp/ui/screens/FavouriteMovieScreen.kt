package com.georgian.moviesearchapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.georgian.moviesearchapp.data.auth.FirebaseAuthManager
import com.georgian.moviesearchapp.ui.navigation.Screen
import com.georgian.moviesearchapp.ui.viewmodel.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteMovieScreen(navController: NavHostController, movieViewModel: MovieViewModel) {
    // Observe the favorite movies and search state from the ViewModel
    val favoriteMovies by movieViewModel.favoriteMovies.collectAsState()
    val searchState by movieViewModel.searchState.collectAsState()
    var selectedTab by remember { mutableStateOf("favorites") }

    // Check if loading or error state
    if (searchState.isLoading) {
        // Show a loading indicator while fetching data
        CircularProgressIndicator(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center))
    } else {
        // Show favorite movies when they are loaded
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Favorite Movies") },
                    actions = {
                        TextButton(onClick = {
                            FirebaseAuthManager.logout {
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(Screen.Search.route) { inclusive = true }
                                }
                            }
                        }
                        ) {
                            Text("Logout")
                        }

                    }
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    selectedTab = selectedTab,
                    onTabSelected = { tab ->
                        if (tab != selectedTab) {
                            selectedTab = tab
                            when (tab) {
                                "search" -> navController.navigate(Screen.Search.route) {
                                    popUpTo(Screen.Search.route) { inclusive = true }
                                }
                                "favorites" -> navController.navigate(Screen.Favorites.route) {
                                    popUpTo(Screen.Favorites.route) { inclusive = true }
                                }
                            }
                        }
                    }
                )
            },
            content = { paddingValues ->
                Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                    // If there are no favorite movies
                    if (favoriteMovies.isEmpty()) {
                        Text("No Favorite Movies", style = MaterialTheme.typography.bodyLarge)
                    } else {
                        // List of favorite movies
                        LazyColumn {
                            items(favoriteMovies) { movie ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                        .clickable {
                                            navController.navigate(Screen.FavoriteEdit.createRoute(movie.imdbID))
                                        },
                                    elevation = CardDefaults.cardElevation(4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth()
                                    ) {
                                        AsyncImage(
                                            model = movie.poster,
                                            contentDescription = "${movie.title} Poster",
                                            modifier = Modifier
                                                .size(100.dp)
                                                .padding(end = 16.dp)
                                        )

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = movie.title,
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "Year: ${movie.year}",
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

