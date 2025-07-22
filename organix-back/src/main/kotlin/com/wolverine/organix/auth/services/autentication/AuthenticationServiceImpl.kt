package com.wolverine.organix.auth.services.autentication

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import com.wolverine.organix.auth.dto.JwtAuthenticationResponse
import com.wolverine.organix.auth.dto.SignUpRequest
import com.wolverine.organix.auth.dto.SigninRequest
import com.wolverine.organix.auth.services.jwt.JwtService
import com.wolverine.organix.users.models.RolUsuario
import com.wolverine.organix.users.models.Usuario
import com.wolverine.organix.users.repositories.UsuarioRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.UUID

@Service
class AuthenticationServiceImpl(
    private val userRepository: UsuarioRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
) : AuthenticationService {

    override fun signup(request: SignUpRequest): JwtAuthenticationResponse {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        val parsedBirthDate: LocalDate? = request.birthDate?.let {
            try {
                LocalDate.parse(it, formatter)
            } catch (e: DateTimeParseException) {
                null
            }
        }

        val randomAvatarIndex = (1..6).random()
        val avatarFinal = request.avatar ?: "https://.../foto_perfil_${randomAvatarIndex}.png"

        val usuario = Usuario(
            id = UUID.randomUUID(),
            usernameField = request.username,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            firstName = request.firstName,
            lastName = request.lastName,
            phone = request.phone,
            avatarUrl = avatarFinal,
            birthDate = parsedBirthDate ?: LocalDate.now(),
            rol = RolUsuario.USUARIO,
            isDeleted = false,
            language = "es",
            timezone = "Europe/Madrid",
            googleDriveLinked = false,
            googleDriveRefreshToken = null,
            dashboardLayoutId = null,
            lastLoginAt = null
        )

        userRepository.save(usuario)

        val token = jwtService.generateToken(usuario)
        return JwtAuthenticationResponse(token, usuario.rol.name, usuario)
    }


    override fun signin(request: SigninRequest): JwtAuthenticationResponse {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.email, request.password)
            )
        } catch (e: BadCredentialsException) {
            throw IllegalArgumentException("Correo o contraseña incorrectos")
        }

        val user = userRepository.findByEmail(request.email)
            .orElseThrow { IllegalArgumentException("Usuario no encontrado") }

        val jwt = jwtService.generateToken(user)
        return JwtAuthenticationResponse(jwt, user.rol.name, user)
    }
}