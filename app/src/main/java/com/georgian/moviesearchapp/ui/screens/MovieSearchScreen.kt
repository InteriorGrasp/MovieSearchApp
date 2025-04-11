package com.georgian.moviesearchapp.ui.screens

//ui for movie search screen
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.georgian.moviesearchapp.data.model.Movie
import com.georgian.moviesearchapp.ui.viewmodel.MovieViewModel
import com.georgian.moviesearchapp.ui.navigation.Screen

@Composable
fun MovieSearchScreen(navController: NavController, viewModel: MovieViewModel) {
    var query by remember { mutableStateOf("") }
    val searchState by viewModel.searchState.collectAsState()
    val movies = viewModel.movies.collectAsState().value

    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize()) {

        // Search TextField and Button
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search Movies") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { viewModel.searchMovies(query) },
            modifier = Modifier.fillMaxWidth(),
            enabled = query.isNotEmpty() && !searchState.isLoading
        ) {
            Text("Search")
        }

        // Loading State and Error Message
        searchState.error?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        if (searchState.isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        // Movie List
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(movies) { movie ->
                MovieItem(movie = movie, navController = navController)
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { navController.navigate(Screen.Detail.createRoute(movie.imdbID)) },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            // Movie Poster
            Image(
                painter = rememberAsyncImagePainter(movie.Poster),
                contentDescription = "Movie Poster",
                modifier = Modifier
                    .size(100.dp)
                    .padding(end = 16.dp),
                alignment = Alignment.Center
            )
            Column {
                Text(
                    text = movie.Title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Year: ${movie.Year }",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
