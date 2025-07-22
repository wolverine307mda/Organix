package com.wolverine.organix.users.dto

import com.wolverine.organix.users.models.RolUsuario
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class UsuarioResponseDto(
    val id: UUID,
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate?,
    val addressLine: String?,
    val city: String?,
    val postalCode: String?,
    val country: String?,
    val phone: String?,
    val avatarUrl: String?,
    val language: String,
    val timezone: String,
    val rol: RolUsuario,
    val lastLoginAt: LocalDateTime?,
    val googleDriveLinked: Boolean,
    val dashboardLayoutId: UUID?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)
