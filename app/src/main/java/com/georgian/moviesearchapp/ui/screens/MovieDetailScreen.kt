package com.georgian.moviesearchapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import coil.compose.rememberAsyncImagePainter
import com.georgian.moviesearchapp.data.model.Movie
import com.georgian.moviesearchapp.ui.viewmodel.MovieViewModel
import androidx.navigation.NavHostController
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import android.widget.Toast
import androidx.compose.foundation.rememberScrollState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    imdbID: String,
    navController: NavHostController,
    movieViewModel: MovieViewModel
) {
    val context = LocalContext.current
    val movieDetail by movieViewModel.movieDetails.collectAsStateWithLifecycle()
    val favoriteMovies by movieViewModel.favoriteMovies.collectAsStateWithLifecycle()
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(imdbID) { movieViewModel.getMovieDetails(imdbID) }
    LaunchedEffect(favoriteMovies, movieDetail) {
        isFavorite = favoriteMovies.any { it.imdbID == imdbID }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movie Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (movieDetail == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Poster Section
                movieDetail!!.poster?.takeIf { it.isNotEmpty() }?.let {
                    Image(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = "Movie Poster",
                        modifier = Modifier
                            .height(300.dp)
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )
                } ?: Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color.Gray)
                        .padding(bottom = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Image Available", color = Color.White, fontWeight = FontWeight.Bold)
                }

                // Movie Info
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = movieDetail!!.title ?: "N/A",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        DetailRow("Released", movieDetail!!.released)
                        DetailRow("Year", movieDetail!!.year)
                        DetailRow("Rated", movieDetail!!.rated)
                        DetailRow("Runtime", movieDetail!!.runtime)
                        DetailRow("Genre", movieDetail!!.genre)
                        DetailRow("Director", movieDetail!!.director)
                        DetailRow("Writer", movieDetail!!.writer)
                        DetailRow("Actors", movieDetail!!.actors)

                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Plot:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(
                            text = movieDetail!!.plot ?: "No plot available",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (isFavorite) {
                            Toast.makeText(context, "Already in favorites!", Toast.LENGTH_SHORT).show()
                        } else {
                            movieDetail?.let {
                                movieViewModel.addFavoriteMovie(
                                    Movie(
                                        id = it.imdbID ?: "",
                                        title = it.title ?: "",
                                        poster = it.poster ?: "",
                                        year = it.year ?: "",
                                        imdbID = it.imdbID ?: "",
                                        type = "movie"
                                    )
                                )
                                Toast.makeText(context, "Movie added to favorites!", Toast.LENGTH_SHORT).show()
                                isFavorite = true
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(if (isFavorite) "Already in Favorites" else "Add to Favorites")
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String?) {
    if (!value.isNullOrEmpty()) {
        Row(
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Text(
                text = "$label: ",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(90.dp)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
