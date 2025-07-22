package com.wolverine.organix.auth.dto

data class SigninRequest(
    val email: String,
    val password: String,
    val deviceInfo: DeviceInfo? = null
)