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

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
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
                FirebaseAuthManager.login(email, password) { success, msg ->
                    if (success) {
                        navController.navigate("movie_search") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        error = msg
                    }
                }
            } else {
                error = "Email and password must not be empty"
            }
        }) {
            Text("Login")
        }

        error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Don’t have an account? Register",
            color = Color.Blue,
            modifier = Modifier.clickable {
                navController.navigate("register") {
                    popUpTo("login")
                }
            }
        )
    }
}
