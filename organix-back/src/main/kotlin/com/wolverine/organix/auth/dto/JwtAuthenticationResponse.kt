package com.wolverine.organix.auth.dto

import com.wolverine.organix.users.models.Usuario

data class JwtAuthenticationResponse(
    val token: String,
    val role: String,
    val user: Usuario? = null
)
