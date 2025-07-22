package com.wolverine.organix.users.exceptions

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class UsuarioExceptionHandler {

    private val logger = LoggerFactory.getLogger(UsuarioExceptionHandler::class.java)

    @ExceptionHandler(UsuarioNotFoundException::class)
    fun handleUsuarioNotFoundException(ex: UsuarioNotFoundException): ResponseEntity<ErrorResponse> {
        logger.warn("Usuario no encontrado: ${ex.message}")
        
        val errorResponse = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.NOT_FOUND.value(),
            error = "Usuario no encontrado",
            message = ex.message ?: "El usuario solicitado no existe",
            path = "/api/usuarios"
        )
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
    }

    @ExceptionHandler(UsuarioAlreadyExistsException::class)
    fun handleUsuarioAlreadyExistsException(ex: UsuarioAlreadyExistsException): ResponseEntity<ErrorResponse> {
        logger.warn("Usuario ya existe: ${ex.message}")
        
        val errorResponse = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.CONFLICT.value(),
            error = "Usuario ya existe",
            message = ex.message ?: "El usuario ya existe en el sistema",
            path = "/api/usuarios"
        )
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse)
    }

    @ExceptionHandler(UsuarioValidationException::class)
    fun handleUsuarioValidationException(ex: UsuarioValidationException): ResponseEntity<ErrorResponse> {
        logger.warn("Error de validación de usuario: ${ex.message}")
        
        val errorResponse = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Error de validación",
            message = ex.message ?: "Los datos del usuario no son válidos",
            path = "/api/usuarios"
        )
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    @ExceptionHandler(UsuarioUnauthorizedException::class)
    fun handleUsuarioUnauthorizedException(ex: UsuarioUnauthorizedException): ResponseEntity<ErrorResponse> {
        logger.warn("Acceso no autorizado: ${ex.message}")
        
        val errorResponse = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.UNAUTHORIZED.value(),
            error = "Acceso no autorizado",
            message = ex.message ?: "No tienes permisos para realizar esta acción",
            path = "/api/usuarios"
        )
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<ValidationErrorResponse> {
        logger.warn("Error de validación de campos: ${ex.message}")
        
        val errors = mutableMapOf<String, String>()
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.defaultMessage ?: "Campo inválido"
            errors[fieldName] = errorMessage
        }
        
        val errorResponse = ValidationErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Error de validación",
            message = "Los datos enviados no son válidos",
            path = "/api/usuarios",
            fieldErrors = errors
        )
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ErrorResponse> {
        logger.error("Error interno del servidor: ${ex.message}", ex)
        
        val errorResponse = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = "Error interno del servidor",
            message = "Ocurrió un error inesperado",
            path = "/api/usuarios"
        )
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }
}

data class ErrorResponse(
    val timestamp: LocalDateTime,
    val status: Int,
    val error: String,
    val message: String,
    val path: String
)

data class ValidationErrorResponse(
    val timestamp: LocalDateTime,
    val status: Int,
    val error: String,
    val message: String,
    val path: String,
    val fieldErrors: Map<String, String>
)
