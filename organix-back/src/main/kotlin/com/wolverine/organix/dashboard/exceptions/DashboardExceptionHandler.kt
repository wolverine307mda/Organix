package com.wolverine.organix.dashboard.exceptions

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class DashboardExceptionHandler {

    private val logger = LoggerFactory.getLogger(DashboardExceptionHandler::class.java)

    @ExceptionHandler(DashboardNotFoundException::class)
    fun handleDashboardNotFoundException(ex: DashboardNotFoundException): ResponseEntity<DashboardErrorResponse> {
        logger.warn("Dashboard no encontrado: ${ex.message}")
        
        val errorResponse = DashboardErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.NOT_FOUND.value(),
            error = "Dashboard no encontrado",
            message = ex.message ?: "El dashboard solicitado no existe",
            path = "/api/dashboard"
        )
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
    }

    @ExceptionHandler(DashboardWidgetNotFoundException::class)
    fun handleDashboardWidgetNotFoundException(ex: DashboardWidgetNotFoundException): ResponseEntity<DashboardErrorResponse> {
        logger.warn("Widget no encontrado: ${ex.message}")
        
        val errorResponse = DashboardErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.NOT_FOUND.value(),
            error = "Widget no encontrado",
            message = ex.message ?: "El widget solicitado no existe",
            path = "/api/dashboard"
        )
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
    }

    @ExceptionHandler(DashboardValidationException::class)
    fun handleDashboardValidationException(ex: DashboardValidationException): ResponseEntity<DashboardErrorResponse> {
        logger.warn("Error de validación del dashboard: ${ex.message}")
        
        val errorResponse = DashboardErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Error de validación",
            message = ex.message ?: "Los datos del dashboard no son válidos",
            path = "/api/dashboard"
        )
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    @ExceptionHandler(DashboardLayoutLimitExceededException::class)
    fun handleDashboardLayoutLimitExceededException(ex: DashboardLayoutLimitExceededException): ResponseEntity<DashboardErrorResponse> {
        logger.warn("Límite de layouts excedido: ${ex.message}")
        
        val errorResponse = DashboardErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.FORBIDDEN.value(),
            error = "Límite de layouts excedido",
            message = ex.message ?: "Se ha alcanzado el límite máximo de layouts",
            path = "/api/dashboard"
        )
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse)
    }

    @ExceptionHandler(DashboardWidgetLimitExceededException::class)
    fun handleDashboardWidgetLimitExceededException(ex: DashboardWidgetLimitExceededException): ResponseEntity<DashboardErrorResponse> {
        logger.warn("Límite de widgets excedido: ${ex.message}")
        
        val errorResponse = DashboardErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.FORBIDDEN.value(),
            error = "Límite de widgets excedido",
            message = ex.message ?: "Se ha alcanzado el límite máximo de widgets",
            path = "/api/dashboard"
        )
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse)
    }

    @ExceptionHandler(UserPreferencesNotFoundException::class)
    fun handleUserPreferencesNotFoundException(ex: UserPreferencesNotFoundException): ResponseEntity<DashboardErrorResponse> {
        logger.warn("Preferencias de usuario no encontradas: ${ex.message}")
        
        val errorResponse = DashboardErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.NOT_FOUND.value(),
            error = "Preferencias no encontradas",
            message = ex.message ?: "Las preferencias del usuario no existen",
            path = "/api/dashboard"
        )
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
    }

    @ExceptionHandler(DashboardUnauthorizedException::class)
    fun handleDashboardUnauthorizedException(ex: DashboardUnauthorizedException): ResponseEntity<DashboardErrorResponse> {
        logger.warn("Acceso no autorizado al dashboard: ${ex.message}")
        
        val errorResponse = DashboardErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.UNAUTHORIZED.value(),
            error = "Acceso no autorizado",
            message = ex.message ?: "No tienes permisos para acceder a este dashboard",
            path = "/api/dashboard"
        )
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<DashboardErrorResponse> {
        logger.error("Error interno del servidor en dashboard: ${ex.message}", ex)
        
        val errorResponse = DashboardErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = "Error interno del servidor",
            message = "Ocurrió un error inesperado en el dashboard",
            path = "/api/dashboard"
        )
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }
}

data class DashboardErrorResponse(
    val timestamp: LocalDateTime,
    val status: Int,
    val error: String,
    val message: String,
    val path: String
)
