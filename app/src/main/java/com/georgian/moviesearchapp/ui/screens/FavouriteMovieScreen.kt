package com.georgian.moviesearchapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.georgian.moviesearchapp.ui.viewmodel.MovieViewModel

@Composable
fun FavoritesScreen(navController: NavHostController, movieViewModel: MovieViewModel) {
    // You can retrieve favorite movies from movieViewModel here
    // For now, we'll show a static list for demonstration purposes.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Favorites", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Example of a button to simulate adding a favorite
        Button(onClick = { /* Add functionality to add a new favorite movie */ }) {
            Text("Add New Favorite")
        }
    }
}

