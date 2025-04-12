package com.georgian.moviesearchapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.georgian.moviesearchapp.data.auth.FirebaseAuthManager
import com.georgian.moviesearchapp.data.model.Movie
import com.georgian.moviesearchapp.ui.navigation.Screen
import com.georgian.moviesearchapp.ui.viewmodel.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieSearchScreen(
    movieViewModel: MovieViewModel,
    navController: NavController
) {
    val searchState by movieViewModel.searchState.collectAsState()
    val movies by movieViewModel.movies.collectAsState()
    var selectedTab by remember { mutableStateOf("search") }
    var query by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movie Search") },
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
                    selectedTab = tab
                    if (tab == "favorites") {
                        navController.navigate(Screen.Favorites.route)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Search for a movie") },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (query.isNotBlank()) {
                            movieViewModel.searchMovies(query)
                        }
                    },
                    modifier = Modifier
                        .height(56.dp)
                ) {
                    Text("Search")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                searchState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                searchState.error != null -> {
                    Text(
                        text = searchState.error ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(movies) { movie ->
                            MovieListItem(movie = movie, navController = navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieListItem(movie: Movie, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(Screen.Detail.createRoute(movie.imdbID))
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = movie.poster,
                contentDescription = "${movie.title} poster",
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp)
            )

            Column {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Year: ${movie.year}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(selectedTab: String, onTabSelected: (String) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedTab == "search",
            onClick = { onTabSelected("search") },
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            label = { Text("Search") }
        )
        NavigationBarItem(
            selected = selectedTab == "favorites",
            onClick = { onTabSelected("favorites") },
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
            label = { Text("Favorites") }
        )
    }
}
