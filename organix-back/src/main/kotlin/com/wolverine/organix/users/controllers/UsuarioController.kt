package com.wolverine.organix.users.controllers

import com.wolverine.organix.users.dto.*
import com.wolverine.organix.users.models.RolUsuario
import com.wolverine.organix.users.services.UsuarioService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = ["http://localhost:4200"])
class UsuarioController(
    private val usuarioService: UsuarioService
) {

    private val logger = LoggerFactory.getLogger(UsuarioController::class.java)

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun createUsuario(@Valid @RequestBody usuarioCreateDto: UsuarioCreateDto): ResponseEntity<UsuarioResponseDto> {
        logger.info("Petición para crear usuario recibida")
        
        return try {
            val usuarioCreado = usuarioService.createUsuario(usuarioCreateDto)
            ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado)
        } catch (e: Exception) {
            logger.error("Error al crear usuario: ${e.message}")
            throw e
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR') or authentication.principal.id == #id")
    fun getUsuarioById(@PathVariable id: UUID): ResponseEntity<UsuarioResponseDto> {
        logger.debug("Petición para obtener usuario por ID: $id")
        
        val usuario = usuarioService.getUsuarioById(id)
        return ResponseEntity.ok(usuario)
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun getUsuarioByEmail(@PathVariable email: String): ResponseEntity<UsuarioResponseDto> {
        logger.debug("Petición para obtener usuario por email: $email")
        
        val usuario = usuarioService.getUsuarioByEmail(email)
        return ResponseEntity.ok(usuario)
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun getUsuarioByUsername(@PathVariable username: String): ResponseEntity<UsuarioResponseDto> {
        logger.debug("Petición para obtener usuario por username: $username")
        
        val usuario = usuarioService.getUsuarioByUsername(username)
        return ResponseEntity.ok(usuario)
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR') or authentication.principal.id == #id")
    fun updateUsuario(
        @PathVariable id: UUID,
        @Valid @RequestBody usuarioUpdateDto: UsuarioUpdateDto
    ): ResponseEntity<UsuarioResponseDto> {
        logger.info("Petición para actualizar usuario con ID: $id")
        
        val usuarioActualizado = usuarioService.updateUsuario(id, usuarioUpdateDto)
        return ResponseEntity.ok(usuarioActualizado)
    }

    @PutMapping("/{id}/password")
    @PreAuthorize("authentication.principal.id == #id")
    fun updatePassword(
        @PathVariable id: UUID,
        @Valid @RequestBody passwordUpdateDto: UsuarioPasswordUpdateDto
    ): ResponseEntity<UsuarioResponseDto> {
        logger.info("Petición para actualizar contraseña de usuario con ID: $id")
        
        val usuarioActualizado = usuarioService.updatePassword(id, passwordUpdateDto)
        return ResponseEntity.ok(usuarioActualizado)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun deleteUsuario(@PathVariable id: UUID): ResponseEntity<Map<String, Any>> {
        logger.info("Petición para eliminar usuario con ID: $id")
        
        val eliminado = usuarioService.deleteUsuario(id)
        return ResponseEntity.ok(mapOf("success" to eliminado, "message" to "Usuario eliminado correctamente"))
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun getAllUsuarios(
        @PageableDefault(
            page = 0,
            size = 20,
            sort = ["createdAt"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable
    ): ResponseEntity<Page<UsuarioResponseDto>> {
        logger.debug("Petición para obtener todos los usuarios")
        
        val usuarios = usuarioService.getAllUsuarios(pageable)
        return ResponseEntity.ok(usuarios)
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun searchUsuarios(
        @RequestParam searchTerm: String,
        @PageableDefault(
            page = 0,
            size = 20,
            sort = ["createdAt"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable
    ): ResponseEntity<Page<UsuarioResponseDto>> {
        logger.debug("Petición para buscar usuarios con término: '$searchTerm'")
        
        val usuarios = usuarioService.searchUsuarios(searchTerm, pageable)
        return ResponseEntity.ok(usuarios)
    }

    @GetMapping("/rol/{rol}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun getUsuariosByRol(
        @PathVariable rol: RolUsuario,
        @PageableDefault(
            page = 0,
            size = 20,
            sort = ["createdAt"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable
    ): ResponseEntity<Page<UsuarioResponseDto>> {
        logger.debug("Petición para obtener usuarios por rol: $rol")
        
        val usuarios = usuarioService.getUsuariosByRol(rol, pageable)
        return ResponseEntity.ok(usuarios)
    }

    @GetMapping("/exists/email/{email}")
    fun existsByEmail(@PathVariable email: String): ResponseEntity<Map<String, Boolean>> {
        logger.debug("Verificando si existe usuario con email: $email")
        
        val exists = usuarioService.existsByEmail(email)
        return ResponseEntity.ok(mapOf("exists" to exists))
    }

    @GetMapping("/exists/username/{username}")
    fun existsByUsername(@PathVariable username: String): ResponseEntity<Map<String, Boolean>> {
        logger.debug("Verificando si existe usuario con username: $username")
        
        val exists = usuarioService.existsByUsername(username)
        return ResponseEntity.ok(mapOf("exists" to exists))
    }

    @PostMapping("/reset-pin")
    fun generateResetPin(@Valid @RequestBody request: UsuarioResetPinRequestDto): ResponseEntity<Map<String, String>> {
        logger.info("Petición para generar PIN de reset para email: ${request.email}")
        
        val pin = usuarioService.generateResetPin(request.email)
        return ResponseEntity.ok(mapOf("message" to "PIN de reset enviado al email", "pin" to pin))
    }

    @PostMapping("/reset-password/{email}")
    fun resetPassword(
        @PathVariable email: String,
        @Valid @RequestBody resetDto: UsuarioResetPasswordDto
    ): ResponseEntity<UsuarioResponseDto> {
        logger.info("Petición para resetear contraseña para email: $email")
        
        val usuario = usuarioService.resetPassword(email, resetDto)
        return ResponseEntity.ok(usuario)
    }

    @PutMapping("/{id}/last-login")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun updateLastLogin(@PathVariable id: UUID): ResponseEntity<UsuarioResponseDto> {
        logger.debug("Actualizando último login para usuario con ID: $id")
        
        val usuario = usuarioService.updateLastLogin(id)
        return ResponseEntity.ok(usuario)
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun getUserStats(): ResponseEntity<Map<String, Any>> {
        logger.debug("Obteniendo estadísticas de usuarios")
        
        val stats = usuarioService.getUserStats()
        return ResponseEntity.ok(stats)
    }

    @PutMapping("/{id}/google-drive")
    @PreAuthorize("authentication.principal.id == #id")
    fun updateGoogleDriveIntegration(
        @PathVariable id: UUID,
        @RequestParam linked: Boolean,
        @RequestParam(required = false) refreshToken: String?
    ): ResponseEntity<UsuarioResponseDto> {
        logger.info("Actualizando integración de Google Drive para usuario con ID: $id")
        
        val usuario = usuarioService.updateGoogleDriveIntegration(id, linked, refreshToken)
        return ResponseEntity.ok(usuario)
    }

    @GetMapping("/google-drive")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun getUsuariosWithGoogleDrive(): ResponseEntity<List<UsuarioResponseDto>> {
        logger.debug("Obteniendo usuarios con Google Drive vinculado")
        
        val usuarios = usuarioService.getUsuariosWithGoogleDrive()
        return ResponseEntity.ok(usuarios)
    }
}
