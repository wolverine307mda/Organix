package com.wolverine.organix.dashboard.dto

import com.wolverine.organix.users.dto.UsuarioResponseDto
import java.time.LocalDateTime
import java.util.*

/**
 * DTO completo que incluye toda la información necesaria para el frontend
 * Incluye usuario, preferencias, layout activo y datos de widgets
 */
data class CompleteDashboardDto(
    val user: UsuarioResponseDto,
    val preferences: UserPreferencesResponseDto,
    val currentLayout: DashboardLayoutResponseDto,
    val availableLayouts: List<DashboardLayoutResponseDto>,
    val widgetsData: Map<UUID, Map<String, Any>>,
    val metadata: DashboardMetadataDto
)

data class DashboardMetadataDto(
    val lastUpdated: LocalDateTime,
    val version: String = "1.0",
    val theme: String,
    val totalWidgets: Int,
    val totalLayouts: Int,
    val isOnline: Boolean = true
)

/**
 * DTO para actualización rápida del dashboard
 */
data class DashboardQuickUpdateDto(
    val layoutId: UUID? = null,
    val preferences: UserPreferencesUpdateDto? = null,
    val widgetUpdates: List<DashboardWidgetUpdateDto>? = null
)

/**
 * DTO para la inicialización del dashboard
 */
data class DashboardInitDto(
    val userId: UUID,
    val createDefaultIfNotExists: Boolean = true,
    val theme: String = "light",
    val includeWidgetData: Boolean = true
)

/**
 * DTO para estadísticas del dashboard
 */
data class DashboardStatsDto(
    val totalLayouts: Int,
    val totalWidgets: Int,
    val mostUsedWidgetType: String,
    val averageWidgetsPerLayout: Double,
    val lastAccessDate: LocalDateTime?,
    val creationDate: LocalDateTime
)

/**
 * DTO para backup/restore del dashboard
 */
data class DashboardBackupDto(
    val version: String = "1.0",
    val backupDate: LocalDateTime,
    val userId: UUID,
    val layouts: List<DashboardLayoutResponseDto>,
    val preferences: UserPreferencesResponseDto,
    val customData: Map<String, Any>? = null
)

/**
 * DTO para configuración de widget específico
 */
data class WidgetConfigDto(
    val widgetType: String,
    val config: Map<String, Any>,
    val defaultConfig: Map<String, Any>,
    val availableOptions: Map<String, Any>
)

/**
 * DTO para respuesta de la API principal del dashboard
 */
data class DashboardApiResponseDto(
    val success: Boolean,
    val data: CompleteDashboardDto?,
    val message: String? = null,
    val errors: List<String>? = null,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
