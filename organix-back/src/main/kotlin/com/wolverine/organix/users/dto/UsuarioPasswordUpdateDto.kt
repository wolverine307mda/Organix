package com.wolverine.organix.users.dto

import jakarta.validation.constraints.Size

data class UsuarioPasswordUpdateDto(
    @field:Size(min = 8, message = "La contraseña actual es obligatoria")
    val currentPassword: String,

    @field:Size(min = 8, message = "La nueva contraseña debe tener al menos 8 caracteres")
    val newPassword: String
)
