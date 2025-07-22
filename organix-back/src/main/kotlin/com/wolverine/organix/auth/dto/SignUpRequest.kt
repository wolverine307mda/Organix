package com.wolverine.organix.auth.dto

data class SignUpRequest(
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val phone: String,
    val avatar: String?, // si no se manda, se genera random
    val birthDate: String? // en formato ISO yyyy-MM-dd
)
