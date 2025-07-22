package com.wolverine.organix.users.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UsuarioResetPasswordDto(
    @field:NotBlank(message = "El PIN es obligatorio")
    @field:Size(min = 6, max = 6, message = "El PIN debe tener 6 dígitos")
    val pin: String,

    @field:NotBlank(message = "La nueva contraseña es obligatoria")
    @field:Size(min = 8, message = "La nueva contraseña debe tener al menos 8 caracteres")
    val newPassword: String
)
