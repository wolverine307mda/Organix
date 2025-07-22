package com.wolverine.organix.users.services

import com.wolverine.organix.users.dto.*
import com.wolverine.organix.users.models.RolUsuario
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface UsuarioService {

    /**
     * Crea un nuevo usuario en el sistema
     */
    fun createUsuario(usuarioCreateDto: UsuarioCreateDto): UsuarioResponseDto

    /**
     * Obtiene un usuario por su ID
     */
    fun getUsuarioById(id: UUID): UsuarioResponseDto

    /**
     * Obtiene un usuario por su email
     */
    fun getUsuarioByEmail(email: String): UsuarioResponseDto

    /**
     * Obtiene un usuario por su username
     */
    fun getUsuarioByUsername(username: String): UsuarioResponseDto

    /**
     * Actualiza los datos de un usuario
     */
    fun updateUsuario(id: UUID, usuarioUpdateDto: UsuarioUpdateDto): UsuarioResponseDto

    /**
     * Actualiza la contraseña de un usuario
     */
    fun updatePassword(id: UUID, passwordUpdateDto: UsuarioPasswordUpdateDto): UsuarioResponseDto

    /**
     * Elimina un usuario (soft delete)
     */
    fun deleteUsuario(id: UUID): Boolean

    /**
     * Obtiene todos los usuarios con paginación
     */
    fun getAllUsuarios(pageable: Pageable): Page<UsuarioResponseDto>

    /**
     * Busca usuarios por término de búsqueda
     */
    fun searchUsuarios(searchTerm: String, pageable: Pageable): Page<UsuarioResponseDto>

    /**
     * Obtiene usuarios por rol
     */
    fun getUsuariosByRol(rol: RolUsuario, pageable: Pageable): Page<UsuarioResponseDto>

    /**
     * Verifica si existe un usuario con el email dado
     */
    fun existsByEmail(email: String): Boolean

    /**
     * Verifica si existe un usuario con el username dado
     */
    fun existsByUsername(username: String): Boolean

    /**
     * Genera un PIN de reset para el usuario
     */
    fun generateResetPin(email: String): String

    /**
     * Resetea la contraseña usando el PIN
     */
    fun resetPassword(email: String, resetDto: UsuarioResetPasswordDto): UsuarioResponseDto

    /**
     * Actualiza el último login del usuario
     */
    fun updateLastLogin(id: UUID): UsuarioResponseDto

    /**
     * Obtiene estadísticas de usuarios
     */
    fun getUserStats(): Map<String, Any>

    /**
     * Vincula/desvincula Google Drive para un usuario
     */
    fun updateGoogleDriveIntegration(id: UUID, linked: Boolean, refreshToken: String? = null): UsuarioResponseDto

    /**
     * Obtiene usuarios con Google Drive vinculado
     */
    fun getUsuariosWithGoogleDrive(): List<UsuarioResponseDto>
}
