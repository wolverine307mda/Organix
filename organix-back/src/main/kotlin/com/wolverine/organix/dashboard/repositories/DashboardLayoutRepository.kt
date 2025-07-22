package com.wolverine.organix.dashboard.repositories

import com.wolverine.organix.dashboard.models.DashboardLayout
import com.wolverine.organix.users.models.Usuario
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DashboardLayoutRepository : JpaRepository<DashboardLayout, UUID> {
    
    fun findByUsuarioAndIsActiveTrue(usuario: Usuario): List<DashboardLayout>
    
    fun findByUsuarioAndIsDefaultTrueAndIsActiveTrue(usuario: Usuario): DashboardLayout?
    
    @Query("SELECT dl FROM DashboardLayout dl WHERE dl.usuario.id = :usuarioId AND dl.isActive = true ORDER BY dl.isDefault DESC, dl.createdAt ASC")
    fun findActiveLayoutsByUsuarioId(@Param("usuarioId") usuarioId: UUID): List<DashboardLayout>
    
    @Query("SELECT dl FROM DashboardLayout dl WHERE dl.id = :id AND dl.usuario.id = :userId")
    fun findByIdAndUserId(@Param("id") id: UUID, @Param("userId") userId: UUID): DashboardLayout?
    
    @Query("SELECT dl FROM DashboardLayout dl WHERE dl.usuario.id = :userId")
    fun findByUserId(@Param("userId") userId: UUID): List<DashboardLayout>
    
    @Query("SELECT dl FROM DashboardLayout dl WHERE dl.usuario.id = :userId AND dl.isDefault = :isDefault")
    fun findByUserIdAndIsDefault(@Param("userId") userId: UUID, @Param("isDefault") isDefault: Boolean): DashboardLayout?
    
    @Query("SELECT dl FROM DashboardLayout dl WHERE dl.usuario.id = :userId AND LOWER(dl.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    fun findByUserIdAndNameContainingIgnoreCase(@Param("userId") userId: UUID, @Param("searchTerm") searchTerm: String, pageable: Pageable): Page<DashboardLayout>
    
    @Query("SELECT dl FROM DashboardLayout dl WHERE dl.usuario.id = :userId AND dl.isDeleted = false")
    fun findByUserIdAndIsDeletedFalse(@Param("userId") userId: UUID, pageable: Pageable): Page<DashboardLayout>
    
    @Query("SELECT dl FROM DashboardLayout dl WHERE dl.usuario.id = :userId AND dl.isDeleted = false")
    fun findByUserIdAndIsDeletedFalse(@Param("userId") userId: UUID): List<DashboardLayout>
    
    fun existsByUsuarioAndIsDefaultTrueAndIdNot(usuario: Usuario, id: UUID): Boolean

    @Query("SELECT dl FROM DashboardLayout dl WHERE dl.id = :id AND dl.usuario.id = :userId AND dl.isDeleted = false")
    fun findByIdAndUserIdAndIsDeletedFalse(@Param("id") id: UUID, @Param("userId") userId: UUID): Optional<DashboardLayout>

    @Query("SELECT CASE WHEN COUNT(dl) > 0 THEN true ELSE false END FROM DashboardLayout dl WHERE dl.usuario.id = :userId AND dl.name = :name AND dl.isDeleted = false")
    fun existsByUserIdAndNameAndIsDeletedFalse(@Param("userId") userId: UUID, @Param("name") name: String): Boolean

    @Query("SELECT CASE WHEN COUNT(dl) > 0 THEN true ELSE false END FROM DashboardLayout dl WHERE dl.usuario.id = :userId AND dl.isDefault = true AND dl.isDeleted = false")
    fun existsByUserIdAndIsDefaultTrueAndIsDeletedFalse(@Param("userId") userId: UUID): Boolean

    @Query("SELECT COUNT(dl) FROM DashboardLayout dl WHERE dl.usuario.id = :userId AND dl.isDeleted = false")
    fun countByUserIdAndIsDeletedFalse(@Param("userId") userId: UUID): Long

    @Query("SELECT dl FROM DashboardLayout dl WHERE dl.userId = :userId AND dl.isDeleted = false AND " +
           "(LOWER(dl.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(dl.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    fun searchByUserIdAndTerm(@Param("userId") userId: UUID, @Param("searchTerm") searchTerm: String, pageable: Pageable): Page<DashboardLayout>

    fun findByUserIdAndThemeAndIsDeletedFalse(userId: UUID, theme: String): List<DashboardLayout>
}
