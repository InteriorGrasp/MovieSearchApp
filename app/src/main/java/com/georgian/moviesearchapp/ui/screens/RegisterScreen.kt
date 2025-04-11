package com.georgian.moviesearchapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.georgian.moviesearchapp.data.auth.FirebaseAuthManager
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun RegisterScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    val db = FirebaseFirestore.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Register", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (email.isNotBlank() && password.isNotBlank()) {
                FirebaseAuthManager.register(email, password) { success, msg ->
                    if (success) {
                        val userId = FirebaseAuthManager.getCurrentUser()?.uid
                        if (userId != null) {
                            db.collection("users").document(userId).set(
                                mapOf(
                                    "email" to email,
                                    "createdAt" to System.currentTimeMillis()
                                )
                            )
                        }

                        navController.navigate("movie_search") {
                            popUpTo("register") { inclusive = true }
                        }
                    } else {
                        error = msg
                    }
                }
            } else {
                error = "All fields are required"
            }
        }) {
            Text("Sign Up")
        }

        error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Already have an account? Login",
            color = Color.Blue,
            modifier = Modifier.clickable {
                navController.navigate("login") {
                    popUpTo("register")
                }
            }
        )
    }
}
