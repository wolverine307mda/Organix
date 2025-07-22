package com.wolverine.organix.users.dto

import com.wolverine.organix.users.models.RolUsuario
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate
import java.util.*

data class UsuarioCreateDto(
    @field:NotBlank(message = "El email es obligatorio")
    @field:Email(message = "El email debe tener un formato válido")
    val email: String,

    @field:NotBlank(message = "El nombre de usuario es obligatorio")
    @field:Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    val username: String,

    @field:NotBlank(message = "El nombre es obligatorio")
    @field:Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    val firstName: String,

    @field:NotBlank(message = "El apellido es obligatorio")
    @field:Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    val lastName: String,

    @field:NotBlank(message = "La contraseña es obligatoria")
    @field:Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    val password: String,

    val birthDate: LocalDate? = null,
    val addressLine: String? = null,
    val city: String? = null,
    val postalCode: String? = null,
    val country: String? = null,
    val phone: String? = null,
    val avatarUrl: String? = null,
    val language: String = "es",
    val timezone: String = "Europe/Madrid",
    val rol: RolUsuario = RolUsuario.USUARIO
)
