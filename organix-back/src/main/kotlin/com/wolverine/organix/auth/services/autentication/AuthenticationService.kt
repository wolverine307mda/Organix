package com.wolverine.organix.auth.services.autentication

import com.wolverine.organix.auth.dto.JwtAuthenticationResponse
import com.wolverine.organix.auth.dto.SignUpRequest
import com.wolverine.organix.auth.dto.SigninRequest

interface AuthenticationService {
    fun signup(request: SignUpRequest): JwtAuthenticationResponse
    fun signin(request: SigninRequest): JwtAuthenticationResponse
}