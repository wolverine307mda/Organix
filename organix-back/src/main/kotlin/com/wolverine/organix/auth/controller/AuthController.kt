package com.wolverine.organix.auth.controller

import com.wolverine.organix.auth.dto.JwtAuthenticationResponse
import com.wolverine.organix.auth.dto.SignUpRequest
import com.wolverine.organix.auth.dto.SigninRequest
import com.wolverine.organix.auth.services.autentication.AuthenticationService
import com.wolverine.organix.utils.email.EmailService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authenticationService: AuthenticationService,
    private val emailService: EmailService,
) {

    @PostMapping("/signup")
    fun signup(@RequestBody request: SignUpRequest): ResponseEntity<JwtAuthenticationResponse> {
        val response = authenticationService.signup(request)
        //nombre completo, unimos nombre y apelido
        val fullName = "${request.firstName} ${request.lastName}"
        // Enviar correo de bienvenida
        // emailService.sendWelcomeEmail(request.email, fullName, request.email, request.username)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PostMapping("/signin")
    fun signin(@RequestBody request: SigninRequest): ResponseEntity<Any> {
        println("🔐 Login attempt: ${request.email}")
        return try {
            val jwtResponse = authenticationService.signin(request)
            println("✅ Login successful for: ${request.email}")
            
            // Crear respuesta en el formato que espera el frontend
            val response = mapOf(
                "success" to true,
                "token" to jwtResponse.token,
                "user" to jwtResponse.user,
                "message" to "Login exitoso",
                "expiresIn" to 3600 // 1 hora en segundos
            )
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            println("❌ Login failed for: ${request.email} - Error: ${e.message}")
            val errorResponse = mapOf(
                "success" to false,
                "message" to (e.message ?: "Error al iniciar sesión")
            )
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse)
        }
    }
}