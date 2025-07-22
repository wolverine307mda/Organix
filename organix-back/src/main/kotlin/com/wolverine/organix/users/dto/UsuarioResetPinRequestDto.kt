package com.wolverine.organix.users.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class UsuarioResetPinRequestDto(
    @field:NotBlank(message = "El email es obligatorio")
    @field:Email(message = "El email debe tener un formato válido")
    val email: String
)
