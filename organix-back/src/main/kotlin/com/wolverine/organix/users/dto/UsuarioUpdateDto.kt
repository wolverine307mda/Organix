package com.wolverine.organix.users.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size
import java.time.LocalDate
import java.util.*

data class UsuarioUpdateDto(
    @field:Email(message = "El email debe tener un formato válido")
    val email: String? = null,

    @field:Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    val username: String? = null,

    @field:Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    val firstName: String? = null,

    @field:Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    val lastName: String? = null,

    val birthDate: LocalDate? = null,
    val addressLine: String? = null,
    val city: String? = null,
    val postalCode: String? = null,
    val country: String? = null,
    val phone: String? = null,
    val avatarUrl: String? = null,
    val language: String? = null,
    val timezone: String? = null,
    val googleDriveLinked: Boolean? = null,
    val dashboardLayoutId: UUID? = null
)
