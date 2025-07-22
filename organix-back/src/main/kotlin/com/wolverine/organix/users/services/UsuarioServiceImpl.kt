package com.wolverine.organix.users.services

import com.wolverine.organix.users.dto.*
import com.wolverine.organix.users.exceptions.*
import com.wolverine.organix.users.mappers.UsuarioMapper
import com.wolverine.organix.users.models.RolUsuario
import com.wolverine.organix.users.models.Usuario
import com.wolverine.organix.users.repositories.UsuarioRepository
import com.wolverine.organix.utils.email.EmailService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
@Transactional
class UsuarioServiceImpl(
    private val usuarioRepository: UsuarioRepository,
    private val usuarioMapper: UsuarioMapper,
    private val passwordEncoder: PasswordEncoder,
    private val emailService: EmailService
) : UsuarioService {

    private val logger = LoggerFactory.getLogger(UsuarioServiceImpl::class.java)

    override fun createUsuario(usuarioCreateDto: UsuarioCreateDto): UsuarioResponseDto {
        logger.info("Iniciando creación de usuario con email: ${usuarioCreateDto.email}")
        
        // Validar que el email no exista
        if (usuarioRepository.existsByEmail(usuarioCreateDto.email)) {
            logger.warn("Intento de crear usuario con email ya existente: ${usuarioCreateDto.email}")
            throw UsuarioAlreadyExistsException("Ya existe un usuario con el email: ${usuarioCreateDto.email}")
        }

        // Validar que el username no exista
        if (usuarioRepository.existsByUsernameField(usuarioCreateDto.username)) {
            logger.warn("Intento de crear usuario con username ya existente: ${usuarioCreateDto.username}")
            throw UsuarioAlreadyExistsException("Ya existe un usuario con el username: ${usuarioCreateDto.username}")
        }

        try {
            val usuario = usuarioMapper.toEntity(usuarioCreateDto)
            usuario.password = passwordEncoder.encode(usuarioCreateDto.password)
            
            val savedUsuario = usuarioRepository.save(usuario)
            logger.info("Usuario creado exitosamente con ID: ${savedUsuario.id}")
            
            return usuarioMapper.toResponseDto(savedUsuario)
        } catch (e: Exception) {
            logger.error("Error al crear usuario: ${e.message}", e)
            throw UsuarioValidationException("Error al crear el usuario: ${e.message}")
        }
    }

    @Transactional(readOnly = true)
    override fun getUsuarioById(id: UUID): UsuarioResponseDto {
        logger.debug("Buscando usuario por ID: $id")
        
        val usuario = usuarioRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow { 
                logger.warn("Usuario no encontrado con ID: $id")
                UsuarioNotFoundException("Usuario no encontrado con ID: $id") 
            }
        
        return usuarioMapper.toResponseDto(usuario)
    }

    @Transactional(readOnly = true)
    override fun getUsuarioByEmail(email: String): UsuarioResponseDto {
        logger.debug("Buscando usuario por email: $email")
        
        val usuario = usuarioRepository.findByEmailAndIsDeletedFalse(email)
            .orElseThrow { 
                logger.warn("Usuario no encontrado con email: $email")
                UsuarioNotFoundException("Usuario no encontrado con email: $email") 
            }
        
        return usuarioMapper.toResponseDto(usuario)
    }

    @Transactional(readOnly = true)
    override fun getUsuarioByUsername(username: String): UsuarioResponseDto {
        logger.debug("Buscando usuario por username: $username")
        
        val usuario = usuarioRepository.findByUsernameFieldAndIsDeletedFalse(username)
            .orElseThrow { 
                logger.warn("Usuario no encontrado con username: $username")
                UsuarioNotFoundException("Usuario no encontrado con username: $username") 
            }
        
        return usuarioMapper.toResponseDto(usuario)
    }

    override fun updateUsuario(id: UUID, usuarioUpdateDto: UsuarioUpdateDto): UsuarioResponseDto {
        logger.info("Actualizando usuario con ID: $id")
        
        val usuario = usuarioRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow { 
                logger.warn("Usuario no encontrado para actualizar con ID: $id")
                UsuarioNotFoundException("Usuario no encontrado con ID: $id") 
            }

        // Validar email único si se está actualizando
        usuarioUpdateDto.email?.let { newEmail ->
            if (newEmail != usuario.email && usuarioRepository.existsByEmail(newEmail)) {
                logger.warn("Intento de actualizar a email ya existente: $newEmail")
                throw UsuarioAlreadyExistsException("Ya existe un usuario con el email: $newEmail")
            }
        }

        // Validar username único si se está actualizando
        usuarioUpdateDto.username?.let { newUsername ->
            if (newUsername != usuario.getNombreUsuario() && usuarioRepository.existsByUsernameField(newUsername)) {
                logger.warn("Intento de actualizar a username ya existente: $newUsername")
                throw UsuarioAlreadyExistsException("Ya existe un usuario con el username: $newUsername")
            }
        }

        try {
            val updatedUsuario = usuarioMapper.updateEntityFromDto(usuario, usuarioUpdateDto)
            val savedUsuario = usuarioRepository.save(updatedUsuario)
            
            logger.info("Usuario actualizado exitosamente con ID: ${savedUsuario.id}")
            return usuarioMapper.toResponseDto(savedUsuario)
        } catch (e: Exception) {
            logger.error("Error al actualizar usuario con ID $id: ${e.message}", e)
            throw UsuarioValidationException("Error al actualizar el usuario: ${e.message}")
        }
    }

    override fun updatePassword(id: UUID, passwordUpdateDto: UsuarioPasswordUpdateDto): UsuarioResponseDto {
        logger.info("Actualizando contraseña para usuario con ID: $id")
        
        val usuario = usuarioRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow { 
                logger.warn("Usuario no encontrado para actualizar contraseña con ID: $id")
                UsuarioNotFoundException("Usuario no encontrado con ID: $id") 
            }

        // Verificar contraseña actual
        if (!passwordEncoder.matches(passwordUpdateDto.currentPassword, usuario.password)) {
            logger.warn("Intento de cambio de contraseña con contraseña actual incorrecta para usuario: $id")
            throw UsuarioUnauthorizedException("La contraseña actual es incorrecta")
        }

        try {
            usuario.password = passwordEncoder.encode(passwordUpdateDto.newPassword)
            val savedUsuario = usuarioRepository.save(usuario)
            
            logger.info("Contraseña actualizada exitosamente para usuario con ID: $id")
            return usuarioMapper.toResponseDto(savedUsuario)
        } catch (e: Exception) {
            logger.error("Error al actualizar contraseña para usuario con ID $id: ${e.message}", e)
            throw UsuarioValidationException("Error al actualizar la contraseña: ${e.message}")
        }
    }

    override fun deleteUsuario(id: UUID): Boolean {
        logger.info("Eliminando usuario con ID: $id")
        
        val usuario = usuarioRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow { 
                logger.warn("Usuario no encontrado para eliminar con ID: $id")
                UsuarioNotFoundException("Usuario no encontrado con ID: $id") 
            }

        try {
            usuario.isDeleted = true
            usuarioRepository.save(usuario)
            
            logger.info("Usuario eliminado exitosamente con ID: $id")
            return true
        } catch (e: Exception) {
            logger.error("Error al eliminar usuario con ID $id: ${e.message}", e)
            throw UsuarioValidationException("Error al eliminar el usuario: ${e.message}")
        }
    }

    @Transactional(readOnly = true)
    override fun getAllUsuarios(pageable: Pageable): Page<UsuarioResponseDto> {
        logger.debug("Obteniendo todos los usuarios con paginación: ${pageable.pageNumber}, ${pageable.pageSize}")
        
        val usuarios = usuarioRepository.findAllByIsDeletedFalse(pageable)
        return usuarios.map { usuarioMapper.toResponseDto(it) }
    }

    @Transactional(readOnly = true)
    override fun searchUsuarios(searchTerm: String, pageable: Pageable): Page<UsuarioResponseDto> {
        logger.debug("Buscando usuarios con término: '$searchTerm'")
        
        val usuarios = usuarioRepository.findBySearchTerm(searchTerm, pageable)
        return usuarios.map { usuarioMapper.toResponseDto(it) }
    }

    @Transactional(readOnly = true)
    override fun getUsuariosByRol(rol: RolUsuario, pageable: Pageable): Page<UsuarioResponseDto> {
        logger.debug("Obteniendo usuarios por rol: $rol")
        
        val usuarios = usuarioRepository.findByRolAndIsDeletedFalse(rol, pageable)
        return usuarios.map { usuarioMapper.toResponseDto(it) }
    }

    @Transactional(readOnly = true)
    override fun existsByEmail(email: String): Boolean {
        return usuarioRepository.existsByEmail(email)
    }

    @Transactional(readOnly = true)
    override fun existsByUsername(username: String): Boolean {
        return usuarioRepository.existsByUsernameField(username)
    }

    override fun generateResetPin(email: String): String {
        logger.info("Generando PIN de reset para email: $email")
        
        val usuario = usuarioRepository.findByEmailAndIsDeletedFalse(email)
            .orElseThrow { 
                logger.warn("Usuario no encontrado para generar PIN con email: $email")
                UsuarioNotFoundException("Usuario no encontrado con email: $email") 
            }

        try {
            val pin = usuario.generateResetPin()
            usuarioRepository.save(usuario)
            
            // Enviar email con el PIN
            emailService.sendResetPinEmail(usuario.email, pin)
            
            logger.info("PIN de reset generado y enviado exitosamente para email: $email")
            return pin
        } catch (e: Exception) {
            logger.error("Error al generar PIN de reset para email $email: ${e.message}", e)
            throw UsuarioValidationException("Error al generar PIN de reset: ${e.message}")
        }
    }

    override fun resetPassword(email: String, resetDto: UsuarioResetPasswordDto): UsuarioResponseDto {
        logger.info("Reseteando contraseña para email: $email")
        
        val usuario = usuarioRepository.findByEmailAndIsDeletedFalse(email)
            .orElseThrow { 
                logger.warn("Usuario no encontrado para reset de contraseña con email: $email")
                UsuarioNotFoundException("Usuario no encontrado con email: $email") 
            }

        if (!usuario.isResetPinValid(resetDto.pin)) {
            logger.warn("PIN de reset inválido o expirado para email: $email")
            throw UsuarioUnauthorizedException("PIN de reset inválido o expirado")
        }

        try {
            usuario.password = passwordEncoder.encode(resetDto.newPassword)
            usuario.clearResetPin()
            val savedUsuario = usuarioRepository.save(usuario)
            
            logger.info("Contraseña reseteada exitosamente para email: $email")
            return usuarioMapper.toResponseDto(savedUsuario)
        } catch (e: Exception) {
            logger.error("Error al resetear contraseña para email $email: ${e.message}", e)
            throw UsuarioValidationException("Error al resetear la contraseña: ${e.message}")
        }
    }

    override fun updateLastLogin(id: UUID): UsuarioResponseDto {
        logger.debug("Actualizando último login para usuario con ID: $id")
        
        val usuario = usuarioRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow { 
                logger.warn("Usuario no encontrado para actualizar último login con ID: $id")
                UsuarioNotFoundException("Usuario no encontrado con ID: $id") 
            }

        try {
            usuario.lastLoginAt = LocalDateTime.now()
            val savedUsuario = usuarioRepository.save(usuario)
            
            return usuarioMapper.toResponseDto(savedUsuario)
        } catch (e: Exception) {
            logger.error("Error al actualizar último login para usuario con ID $id: ${e.message}", e)
            throw UsuarioValidationException("Error al actualizar último login: ${e.message}")
        }
    }

    @Transactional(readOnly = true)
    override fun getUserStats(): Map<String, Any> {
        logger.debug("Obteniendo estadísticas de usuarios")
        
        val totalUsers = usuarioRepository.countActiveUsers()
        val activeUsersLastMonth = usuarioRepository.countActiveUsersSince(LocalDateTime.now().minusMonths(1))
        val activeUsersLastWeek = usuarioRepository.countActiveUsersSince(LocalDateTime.now().minusWeeks(1))
        
        return mapOf(
            "totalUsers" to totalUsers,
            "activeUsersLastMonth" to activeUsersLastMonth,
            "activeUsersLastWeek" to activeUsersLastWeek
        )
    }

    override fun updateGoogleDriveIntegration(id: UUID, linked: Boolean, refreshToken: String?): UsuarioResponseDto {
        logger.info("Actualizando integración de Google Drive para usuario con ID: $id, linked: $linked")
        
        val usuario = usuarioRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow { 
                logger.warn("Usuario no encontrado para actualizar Google Drive con ID: $id")
                UsuarioNotFoundException("Usuario no encontrado con ID: $id") 
            }

        try {
            usuario.googleDriveLinked = linked
            usuario.googleDriveRefreshToken = if (linked) refreshToken else null
            val savedUsuario = usuarioRepository.save(usuario)
            
            logger.info("Integración de Google Drive actualizada exitosamente para usuario con ID: $id")
            return usuarioMapper.toResponseDto(savedUsuario)
        } catch (e: Exception) {
            logger.error("Error al actualizar integración de Google Drive para usuario con ID $id: ${e.message}", e)
            throw UsuarioValidationException("Error al actualizar integración de Google Drive: ${e.message}")
        }
    }

    @Transactional(readOnly = true)
    override fun getUsuariosWithGoogleDrive(): List<UsuarioResponseDto> {
        logger.debug("Obteniendo usuarios con Google Drive vinculado")
        
        val usuarios = usuarioRepository.findByGoogleDriveLinkedTrueAndIsDeletedFalse()
        return usuarioMapper.toResponseDtoList(usuarios)
    }
}
