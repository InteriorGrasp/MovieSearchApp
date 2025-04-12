package com.georgian.moviesearchapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.georgian.moviesearchapp.ui.viewmodel.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteEditScreen(
    movieId: String,
    navController: NavHostController,
    movieViewModel: MovieViewModel
) {
    val movie = movieViewModel.favoriteMovies.collectAsState().value.find { it.imdbID == movieId }

    var year by remember { mutableStateOf(movie?.year ?: "") }

    if (movie == null) {
        Text("Movie not found")
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Favorite Movie") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Poster Image
            AsyncImage(
                model = movie.poster,
                contentDescription = "${movie.title} Poster",
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Title (read-only)
            Text(
                text = movie.title,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Editable Year
            OutlinedTextField(
                value = year,
                onValueChange = { year = it },
                label = { Text("Year") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Save Changes Button
            Button(
                onClick = {
                    val updatedMovie = movie.copy(year = year)
                    movieViewModel.updateFavoriteMovie(updatedMovie)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Delete Button
            Button(
                onClick = {
                    movieViewModel.deleteFavoriteMovie(movie)
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete Movie", color = Color.White)
            }
        }
    }
}
