package com.georgian.moviesearchapp.data.auth

import com.google.firebase.auth.FirebaseAuth

object FirebaseAuthManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun register(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                onResult(it.isSuccessful, it.exception?.message)
            }
    }

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                onResult(it.isSuccessful, it.exception?.message)
            }
    }

    fun logout(onComplete: () -> Unit) {
        FirebaseAuth.getInstance().signOut()
        onComplete()
    }

    fun getCurrentUser() = auth.currentUser

}
