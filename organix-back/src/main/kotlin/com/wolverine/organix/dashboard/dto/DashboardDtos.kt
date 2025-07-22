package com.wolverine.organix.dashboard.dto

import com.wolverine.organix.dashboard.models.WidgetType
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime
import java.util.*

// =============== LAYOUT DTOs ===============

data class DashboardLayoutCreateDto(
    @field:NotBlank(message = "El nombre del layout es obligatorio")
    val name: String,
    val description: String? = null,
    val isDefault: Boolean = false,
    @field:Min(value = 6, message = "El grid debe tener al menos 6 columnas")
    @field:Max(value = 24, message = "El grid no puede tener más de 24 columnas")
    val gridColumns: Int = 12,
    @field:Min(value = 4, message = "El grid debe tener al menos 4 filas")
    @field:Max(value = 16, message = "El grid no puede tener más de 16 filas")
    val gridRows: Int = 8,
    val backgroundColor: String? = null,
    val backgroundImage: String? = null,
    val theme: String = "light"
)

data class DashboardLayoutUpdateDto(
    val name: String? = null,
    val description: String? = null,
    val isDefault: Boolean? = null,
    val gridColumns: Int? = null,
    val gridRows: Int? = null,
    val backgroundColor: String? = null,
    val backgroundImage: String? = null,
    val theme: String? = null
)

data class DashboardLayoutResponseDto(
    val id: UUID,
    val userId: UUID,
    val name: String,
    val description: String?,
    val isDefault: Boolean,
    val gridColumns: Int,
    val gridRows: Int,
    val backgroundColor: String?,
    val backgroundImage: String?,
    val theme: String,
    val widgets: List<DashboardWidgetResponseDto>,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)

data class DashboardLayoutDto(
    val id: UUID?,
    val name: String,
    val description: String?,
    val isDefault: Boolean,
    val isActive: Boolean,
    val widgets: List<DashboardWidgetDto>,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)

// =============== WIDGET DTOs ===============

data class DashboardWidgetRequestDto(
    @field:NotNull(message = "El tipo de widget es obligatorio")
    val widgetType: WidgetType,
    @field:NotBlank(message = "El título del widget es obligatorio")
    val title: String,
    val description: String? = null,
    @field:Min(value = 0, message = "La posición X debe ser mayor o igual a 0")
    val xPosition: Int,
    @field:Min(value = 0, message = "La posición Y debe ser mayor o igual a 0")
    val yPosition: Int,
    @field:Min(value = 1, message = "El ancho debe ser al menos 1")
    val width: Int,
    @field:Min(value = 1, message = "La altura debe ser al menos 1")
    val height: Int,
    val isVisible: Boolean = true,
    val isMinimized: Boolean = false,
    val zIndex: Int = 0,
    val widgetConfig: Map<String, Any>? = null
)

data class DashboardWidgetUpdateDto(
    val title: String? = null,
    val description: String? = null,
    val xPosition: Int? = null,
    val yPosition: Int? = null,
    val width: Int? = null,
    val height: Int? = null,
    val isVisible: Boolean? = null,
    val isMinimized: Boolean? = null,
    val zIndex: Int? = null,
    val widgetConfig: Map<String, Any>? = null
)

data class DashboardWidgetResponseDto(
    val id: UUID,
    val dashboardLayoutId: UUID,
    val widgetType: WidgetType,
    val title: String,
    val description: String?,
    val xPosition: Int,
    val yPosition: Int,
    val width: Int,
    val height: Int,
    val isVisible: Boolean,
    val isMinimized: Boolean,
    val zIndex: Int,
    val widgetConfig: Map<String, Any>?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)

data class DashboardWidgetDto(
    val id: UUID?,
    val type: WidgetType,
    val title: String,
    val description: String?,
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
    val configuration: Map<String, Any>?,
    val isVisible: Boolean,
    val isMinimized: Boolean,
    val orderIndex: Int,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)

data class DashboardWidgetPositionDto(
    val xPosition: Int,
    val yPosition: Int,
    val width: Int? = null,
    val height: Int? = null
)

data class DashboardWidgetPositionUpdateDto(
    val widgetId: UUID,
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int
)

data class DashboardWidgetDataDto(
    val widgetId: UUID,
    val widgetType: WidgetType,
    val data: Map<String, Any>
)

// =============== USER PREFERENCES DTOs ===============

data class UserPreferencesResponseDto(
    val userId: UUID,
    val dashboardTheme: String,
    val sidebarCollapsed: Boolean,
    val language: String?,
    val timezone: String?,
    val notifications: Boolean,
    val autoSave: Boolean,
    val gridColumns: Int,
    val gridRows: Int
)

data class UserPreferencesUpdateDto(
    val dashboardTheme: String? = null,
    val sidebarCollapsed: Boolean? = null,
    val notifications: Boolean? = null,
    val autoSave: Boolean? = null,
    val gridColumns: Int? = null,
    val gridRows: Int? = null
)

// =============== REQUEST DTOs ===============

data class CreateDashboardLayoutRequest(
    val name: String,
    val description: String?,
    val isDefault: Boolean = false
)

data class UpdateDashboardLayoutRequest(
    val name: String?,
    val description: String?,
    val isDefault: Boolean?,
    val isActive: Boolean?
)

data class CreateDashboardWidgetRequest(
    val type: WidgetType,
    val title: String,
    val description: String?,
    val x: Int = 0,
    val y: Int = 0,
    val width: Int = 4,
    val height: Int = 3,
    val configuration: Map<String, Any>?,
    val isVisible: Boolean = true,
    val orderIndex: Int = 0
)

data class UpdateDashboardWidgetRequest(
    val title: String?,
    val description: String?,
    val x: Int?,
    val y: Int?,
    val width: Int?,
    val height: Int?,
    val configuration: Map<String, Any>?,
    val isVisible: Boolean?,
    val isMinimized: Boolean?,
    val orderIndex: Int?
)

data class WidgetPositionRequest(
    val x: Int,
    val y: Int,
    val width: Int?,
    val height: Int?
)
