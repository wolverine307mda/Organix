package com.wolverine.organix.auth.services.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import com.wolverine.organix.config.auth.JwtConfig
import com.wolverine.organix.users.models.Usuario
import com.wolverine.organix.users.repositories.UsuarioRepository
import java.util.*

@Service
class JwtServiceImpl(
    private val jwtConfig: JwtConfig,
    private val usuarioRepository: UsuarioRepository
) : JwtService {

    // Cambiar esto para usar consistentemente el email como subject
    override fun extractUserName(token: String): String {
        return extractClaim(token) { it.subject } // Usar subject en lugar de claim "email"
    }

    override fun generateToken(userDetails: UserDetails): String {
        return generateToken(emptyMap(), userDetails)
    }

    override fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUserName(token) // Ahora usa el subject (email)
        return (username == userDetails.username) && !isTokenExpired(token)
    }

    private fun <T> extractClaim(token: String, claimsResolver: (DecodedJWT) -> T): T {
        val decodedJWT = JWT.decode(token)
        return claimsResolver(decodedJWT)
    }

    private fun generateToken(extraClaims: Map<String, Any>, userDetails: UserDetails): String {
        val now = Date()
        val expirationDate = Date(now.time + (1000 * jwtConfig.expiration))
        val algorithm = Algorithm.HMAC512(getSigningKey())

        val usuario = userDetails as Usuario

        return JWT.create()
            .withHeader(mapOf("typ" to "JWT"))
            .withSubject(usuario.email)
            .withIssuedAt(now)
            .withExpiresAt(expirationDate)
            .withClaim("userId", usuario.id.toString())
            .withClaim("role", usuario.rol.toString())
            .withClaim("email", usuario.email)
            .withClaim("username", usuario.username)
            .sign(algorithm)
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token) { it.expiresAt }
    }

    private fun getSigningKey(): ByteArray {
        return Base64.getEncoder().encode(jwtConfig.secret.toByteArray())
    }

    override fun getUserIdFromToken(username: String): UUID {
        val usuario = usuarioRepository.findByEmail(username).orElse(null)
            ?: usuarioRepository.findByUsernameField(username).orElse(null)
            ?: throw RuntimeException("Usuario no encontrado")
        return usuario.id
    }
}