package com.wolverine.organix.users.repositories

import com.wolverine.organix.users.models.Usuario
import com.wolverine.organix.users.models.RolUsuario
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface UsuarioRepository : JpaRepository<Usuario, UUID> {

    fun findByEmail(email: String): Optional<Usuario>

    fun findByUsernameField(username: String): Optional<Usuario>

    fun existsByEmail(email: String): Boolean

    fun existsByUsernameField(username: String): Boolean

    fun findByEmailAndIsDeletedFalse(email: String): Optional<Usuario>

    fun findByUsernameFieldAndIsDeletedFalse(username: String): Optional<Usuario>

    fun findByIdAndIsDeletedFalse(id: UUID): Optional<Usuario>

    fun findAllByIsDeletedFalse(pageable: Pageable): Page<Usuario>

    fun findByRolAndIsDeletedFalse(rol: RolUsuario, pageable: Pageable): Page<Usuario>

    fun findByResetPin(pin: String): Optional<Usuario>

    @Query("SELECT u FROM Usuario u WHERE u.isDeleted = false AND " +
           "(LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.usernameField) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    fun findBySearchTerm(@Param("searchTerm") searchTerm: String, pageable: Pageable): Page<Usuario>

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.isDeleted = false")
    fun countActiveUsers(): Long

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.isDeleted = false AND u.lastLoginAt >= :since")
    fun countActiveUsersSince(@Param("since") since: LocalDateTime): Long

    fun findByGoogleDriveLinkedTrueAndIsDeletedFalse(): List<Usuario>
}