package com.wolverine.organix.auth.services.jwt

import org.springframework.security.core.userdetails.UserDetails
import java.util.*

interface JwtService {
    fun extractUserName(token: String): String
    fun generateToken(userDetails: UserDetails): String
    fun isTokenValid(token: String, userDetails: UserDetails): Boolean
    fun getUserIdFromToken(username: String): UUID
}