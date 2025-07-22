package com.wolverine.organix.dashboard.controllers

import com.wolverine.organix.dashboard.services.DashboardService
import com.wolverine.organix.users.services.UsuarioService
import com.wolverine.organix.auth.services.jwt.JwtService
import com.wolverine.organix.dashboard.models.WidgetType
import com.wolverine.organix.dashboard.dto.*
import com.wolverine.organix.dashboard.exceptions.*
import org.springframework.web.bind.annotation.*
import org.springframework.validation.annotation.Validated
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import jakarta.validation.Valid
import java.util.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/dashboard")
@Validated
class DashboardController(
    private val dashboardService: DashboardService,
    private val usuarioService: UsuarioService,
    private val jwtService: JwtService
) {
    private val logger = LoggerFactory.getLogger(DashboardController::class.java)

    @GetMapping
    fun getUserDashboards(authentication: Authentication): ResponseEntity<List<DashboardLayoutDto>> {
        val username = authentication.name
        val usuarioId = jwtService.getUserIdFromToken(username)
        val dashboards = dashboardService.getUserDashboards(usuarioId)
        return ResponseEntity.ok(dashboards)
    }

    @GetMapping("/default")
    fun getUserDefaultDashboard(authentication: Authentication): ResponseEntity<DashboardLayoutDto> {
        val username = authentication.name
        val usuarioId = jwtService.getUserIdFromToken(username)
        val dashboard = dashboardService.getUserDefaultDashboard(usuarioId)
        return ResponseEntity.ok(dashboard)
    }

    @PostMapping
    fun createDashboard(
        @RequestBody request: DashboardLayoutCreateDto,
        authentication: Authentication
    ): ResponseEntity<DashboardLayoutDto> {
        val username = authentication.name
        val usuarioId = jwtService.getUserIdFromToken(username)
        val dashboard = dashboardService.createDashboard(usuarioId, request)
        return ResponseEntity.ok(dashboard)
    }

    // =============== DASHBOARD LAYOUT ENDPOINTS ===============

    @PostMapping("/layouts")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun createDashboardLayout(
        @RequestParam userId: UUID,
        @Valid @RequestBody layoutDto: DashboardLayoutCreateDto
    ): ResponseEntity<DashboardLayoutResponseDto> {
        logger.info("Petición para crear layout de dashboard para usuario: $userId")
        
        val layout = dashboardService.createDashboardLayout(userId, layoutDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(layout)
    }

    @GetMapping("/layouts")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun getUserDashboardLayouts(
        @RequestParam userId: UUID,
        @PageableDefault(
            page = 0,
            size = 10,
            sort = ["createdAt"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable
    ): ResponseEntity<Page<DashboardLayoutResponseDto>> {
        logger.debug("Petición para obtener layouts de dashboard para usuario: {}", userId)
        
        val layouts = dashboardService.getUserDashboardLayouts(userId, pageable)
        return ResponseEntity.ok(layouts)
    }

    @GetMapping("/layouts/{layoutId}")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun getDashboardLayout(
        @RequestParam userId: UUID,
        @PathVariable layoutId: UUID
    ): ResponseEntity<DashboardLayoutResponseDto> {
        logger.debug("Petición para obtener layout: {} para usuario: {}", layoutId, userId)
        
        val layout = dashboardService.getDashboardLayout(userId, layoutId)
        return ResponseEntity.ok(layout)
    }

    @GetMapping("/layouts/default")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun getDefaultDashboardLayout(
        @RequestParam userId: UUID
    ): ResponseEntity<DashboardLayoutResponseDto> {
        logger.debug("Petición para obtener layout por defecto para usuario: {}", userId)
        
        val layout = dashboardService.getDefaultDashboardLayout(userId)
        return ResponseEntity.ok(layout)
    }

    @PutMapping("/layouts/{layoutId}")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun updateDashboardLayout(
        @RequestParam userId: UUID,
        @PathVariable layoutId: UUID,
        @Valid @RequestBody layoutDto: DashboardLayoutUpdateDto
    ): ResponseEntity<DashboardLayoutResponseDto> {
        logger.info("Petición para actualizar layout: $layoutId para usuario: $userId")
        
        val layout = dashboardService.updateDashboardLayout(userId, layoutId, layoutDto)
        return ResponseEntity.ok(layout)
    }

    @DeleteMapping("/layouts/{layoutId}")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun deleteDashboardLayout(
        @RequestParam userId: UUID,
        @PathVariable layoutId: UUID
    ): ResponseEntity<Map<String, Any>> {
        logger.info("Petición para eliminar layout: $layoutId para usuario: $userId")
        
        val deleted = dashboardService.deleteDashboardLayout(userId, layoutId)
        return ResponseEntity.ok(mapOf("success" to deleted, "message" to "Layout eliminado correctamente"))
    }

    @PutMapping("/layouts/{layoutId}/set-default")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun setDefaultDashboardLayout(
        @RequestParam userId: UUID,
        @PathVariable layoutId: UUID
    ): ResponseEntity<DashboardLayoutResponseDto> {
        logger.info("Petición para establecer layout por defecto: $layoutId para usuario: $userId")
        
        val layout = dashboardService.setDefaultDashboardLayout(userId, layoutId)
        return ResponseEntity.ok(layout)
    }

    @GetMapping("/layouts/search")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun searchDashboardLayouts(
        @RequestParam userId: UUID,
        @RequestParam searchTerm: String,
        @PageableDefault(
            page = 0,
            size = 10,
            sort = ["createdAt"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable
    ): ResponseEntity<Page<DashboardLayoutResponseDto>> {
        logger.debug("Petición para buscar layouts con término: '{}' para usuario: {}", searchTerm, userId)
        
        val layouts = dashboardService.searchDashboardLayouts(userId, searchTerm, pageable)
        return ResponseEntity.ok(layouts)
    }

    @PostMapping("/layouts/{layoutId}/duplicate")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun duplicateDashboardLayout(
        @RequestParam userId: UUID,
        @PathVariable layoutId: UUID,
        @RequestParam newName: String
    ): ResponseEntity<DashboardLayoutResponseDto> {
        logger.info("Petición para duplicar layout: $layoutId para usuario: $userId")
        
        val layout = dashboardService.duplicateDashboardLayout(userId, layoutId, newName)
        return ResponseEntity.status(HttpStatus.CREATED).body(layout)
    }

    // =============== WIDGET ENDPOINTS ===============

    @PostMapping("/layouts/{layoutId}/widgets")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun createWidget(
        @RequestParam userId: UUID,
        @PathVariable layoutId: UUID,
        @Valid @RequestBody widgetDto: DashboardWidgetRequestDto
    ): ResponseEntity<DashboardWidgetResponseDto> {
        logger.info("Petición para crear widget en layout: $layoutId para usuario: $userId")
        
        val widget = dashboardService.createWidget(userId, layoutId, widgetDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(widget)
    }

    @GetMapping("/layouts/{layoutId}/widgets")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun getLayoutWidgets(
        @RequestParam userId: UUID,
        @PathVariable layoutId: UUID
    ): ResponseEntity<List<DashboardWidgetResponseDto>> {
        logger.debug("Petición para obtener widgets del layout: {} para usuario: {}", layoutId, userId)
        
        val widgets = dashboardService.getLayoutWidgets(userId, layoutId)
        return ResponseEntity.ok(widgets)
    }

    @GetMapping("/layouts/{layoutId}/widgets/{widgetId}")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun getWidget(
        @RequestParam userId: UUID,
        @PathVariable layoutId: UUID,
        @PathVariable widgetId: UUID
    ): ResponseEntity<DashboardWidgetResponseDto> {
        logger.debug("Petición para obtener widget: {} del layout: {} para usuario: {}", widgetId, layoutId, userId)
        
        val widget = dashboardService.getWidget(userId, layoutId, widgetId)
        return ResponseEntity.ok(widget)
    }

    @PutMapping("/layouts/{layoutId}/widgets/{widgetId}")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun updateWidget(
        @RequestParam userId: UUID,
        @PathVariable layoutId: UUID,
        @PathVariable widgetId: UUID,
        @Valid @RequestBody widgetDto: DashboardWidgetUpdateDto
    ): ResponseEntity<DashboardWidgetResponseDto> {
        logger.info("Petición para actualizar widget: $widgetId del layout: $layoutId para usuario: $userId")
        
        val widget = dashboardService.updateWidget(userId, layoutId, widgetId, widgetDto)
        return ResponseEntity.ok(widget)
    }

    @DeleteMapping("/layouts/{layoutId}/widgets/{widgetId}")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun deleteWidget(
        @RequestParam userId: UUID,
        @PathVariable layoutId: UUID,
        @PathVariable widgetId: UUID
    ): ResponseEntity<Map<String, Any>> {
        logger.info("Petición para eliminar widget: $widgetId del layout: $layoutId para usuario: $userId")
        
        val deleted = dashboardService.deleteWidget(userId, layoutId, widgetId)
        return ResponseEntity.ok(mapOf("success" to deleted, "message" to "Widget eliminado correctamente"))
    }

    @PutMapping("/layouts/{layoutId}/widgets/positions")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun updateWidgetPositions(
        @RequestParam userId: UUID,
        @PathVariable layoutId: UUID,
        @RequestBody positions: List<DashboardWidgetPositionUpdateDto>
    ): ResponseEntity<List<DashboardWidgetResponseDto>> {
        logger.info("Petición para actualizar posiciones de widgets en layout: $layoutId para usuario: $userId")
        
        val widgets = dashboardService.updateWidgetPositions(userId, layoutId, positions)
        return ResponseEntity.ok(widgets)
    }

    @GetMapping("/widgets/type/{widgetType}")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun getWidgetsByType(
        @RequestParam userId: UUID,
        @PathVariable widgetType: WidgetType
    ): ResponseEntity<List<DashboardWidgetResponseDto>> {
        logger.debug("Petición para obtener widgets por tipo: {} para usuario: {}", widgetType, userId)
        
        val widgets = dashboardService.getWidgetsByType(userId, widgetType)
        return ResponseEntity.ok(widgets)
    }

    @GetMapping("/layouts/{layoutId}/widgets/{widgetId}/data")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun getWidgetData(
        @RequestParam userId: UUID,
        @PathVariable layoutId: UUID,
        @PathVariable widgetId: UUID
    ): ResponseEntity<DashboardWidgetDataDto> {
        logger.debug("Petición para obtener datos del widget: {}", widgetId)
        
        val data = dashboardService.getWidgetData(userId, layoutId, widgetId)
        return ResponseEntity.ok(data)
    }

    @GetMapping("/layouts/{layoutId}/data")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun getDashboardData(
        @RequestParam userId: UUID,
        @PathVariable layoutId: UUID
    ): ResponseEntity<Map<String, Any>> {
        logger.debug("Petición para obtener datos completos del dashboard: {}", layoutId)
        
        val data = dashboardService.getDashboardData(userId, layoutId)
        return ResponseEntity.ok(data)
    }

    // =============== USER PREFERENCES ENDPOINTS ===============

    @GetMapping("/preferences")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun getUserPreferences(
        @RequestParam userId: UUID
    ): ResponseEntity<UserPreferencesResponseDto> {
        logger.debug("Petición para obtener preferencias de usuario: {}", userId)
        
        val preferences = dashboardService.getUserPreferences(userId)
        return ResponseEntity.ok(preferences)
    }

    @PutMapping("/preferences")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun updateUserPreferences(
        @RequestParam userId: UUID,
        @Valid @RequestBody preferencesDto: UserPreferencesUpdateDto
    ): ResponseEntity<UserPreferencesResponseDto> {
        logger.info("Petición para actualizar preferencias de usuario: $userId")
        
        val preferences = dashboardService.updateUserPreferences(userId, preferencesDto)
        return ResponseEntity.ok(preferences)
    }

    @PostMapping("/preferences/default")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun createDefaultUserPreferences(
        @RequestParam userId: UUID
    ): ResponseEntity<UserPreferencesResponseDto> {
        logger.info("Petición para crear preferencias por defecto para usuario: $userId")
        
        val preferences = dashboardService.createDefaultUserPreferences(userId)
        return ResponseEntity.status(HttpStatus.CREATED).body(preferences)
    }

    // =============== EXPORT/IMPORT ENDPOINTS ===============

    @GetMapping("/layouts/{layoutId}/export")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun exportDashboardConfig(
        @RequestParam userId: UUID,
        @PathVariable layoutId: UUID
    ): ResponseEntity<Map<String, Any>> {
        logger.info("Petición para exportar configuración del dashboard: $layoutId")
        
        val config = dashboardService.exportDashboardConfig(userId, layoutId)
        return ResponseEntity.ok(config)
    }

    @PostMapping("/layouts/import")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun importDashboardConfig(
        @RequestParam userId: UUID,
        @RequestBody config: Map<String, Any>
    ): ResponseEntity<DashboardLayoutResponseDto> {
        logger.info("Petición para importar configuración del dashboard para usuario: $userId")
        
        val layout = dashboardService.importDashboardConfig(userId, config)
        return ResponseEntity.status(HttpStatus.CREATED).body(layout)
    }

    // =============== UTILITY ENDPOINTS ===============

    @GetMapping("/widget-types")
    fun getAvailableWidgetTypes(): ResponseEntity<List<Map<String, Any>>> {
        logger.debug("Petición para obtener tipos de widgets disponibles")
        
        val widgetTypes = WidgetType.entries.map { type ->
            mapOf(
                "type" to type.name,
                "displayName" to type.name.replace("_", " ").lowercase()
                    .split(" ")
                    .joinToString(" ") { it.capitalize() },
                "description" to getWidgetTypeDescription(type)
            )
        }
        
        return ResponseEntity.ok(widgetTypes)
    }

    private fun getWidgetTypeDescription(type: WidgetType): String {
        return when (type) {
            WidgetType.WELCOME -> "Widget de bienvenida"
            WidgetType.QUICK_STATS -> "Estadísticas rápidas"
            WidgetType.CALENDAR -> "Calendario"
            WidgetType.NOTES -> "Notas rápidas"
            WidgetType.WEATHER -> "Información del clima actual"
            WidgetType.TASKS -> "Lista de tareas"
            WidgetType.RECENT_ACTIVITY -> "Actividad reciente"
            WidgetType.CLOCK -> "Reloj mundial"
            WidgetType.SHORTCUTS -> "Accesos directos"
            WidgetType.RSS_FEED -> "Feed de noticias"
            WidgetType.TASKS_SUMMARY -> "Resumen de tareas pendientes y completadas"
            WidgetType.CALENDAR_EVENTS -> "Próximos eventos del calendario"
            WidgetType.QUICK_NOTES -> "Notas rápidas y post-its"
            WidgetType.WEATHER_WIDGET -> "Widget del tiempo"
            WidgetType.HABITS_TRACKER -> "Seguimiento de hábitos diarios"
            WidgetType.RECENT_DOCUMENTS -> "Documentos recientes"
            WidgetType.PHOTO_MEMORIES -> "Recuerdos fotográficos"
            WidgetType.POMODORO_TIMER -> "Temporizador Pomodoro"
            WidgetType.REMINDERS -> "Recordatorios activos"
            WidgetType.RECENT_ACTIVITIES -> "Actividades recientes"
            WidgetType.CUSTOM_HTML -> "Widget personalizado con HTML"
            WidgetType.NOTES_PREVIEW -> "Vista previa de notas"
            WidgetType.FILE_MANAGER -> "Gestor de archivos"
            WidgetType.GOOGLE_CALENDAR -> "Integración con Google Calendar"
            WidgetType.MOTIVATIONAL_QUOTE -> "Cita motivacional del día"
            WidgetType.PROGRESS_TRACKER -> "Seguimiento de progreso"
            WidgetType.NOTIFICATIONS -> "Centro de notificaciones"
            WidgetType.SEARCH_WIDGET -> "Widget de búsqueda"
            WidgetType.SHORTCUT_MENU -> "Menú de accesos directos"
        }
    }

    // =============== MAIN DASHBOARD ENDPOINT ===============

    @GetMapping("/complete")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun getCompleteDashboard(
        @RequestParam userId: UUID,
        @RequestParam(required = false) layoutId: UUID? = null,
        @RequestParam(defaultValue = "true") includeWidgetData: Boolean = true
    ): ResponseEntity<DashboardApiResponseDto> {
        logger.info("Petición para obtener dashboard completo para usuario: $userId")
        
        return try {
            // Obtener usuario
            val user = usuarioService.getUsuarioById(userId)
            
            // Obtener preferencias
            val preferences = dashboardService.getUserPreferences(userId)
            
            // Obtener layout actual (especificado o por defecto)
            val currentLayout = if (layoutId != null) {
                dashboardService.getDashboardLayout(userId, layoutId)
            } else {
                dashboardService.getDefaultDashboardLayout(userId)
            }
            
            // Obtener todos los layouts disponibles
            val allLayouts = dashboardService.getUserDashboardLayouts(userId, Pageable.unpaged())
            
            // Obtener datos de widgets si se solicita
            val widgetsData = if (includeWidgetData) {
                currentLayout.widgets.associate { widget ->
                    widget.id to try {
                        dashboardService.getWidgetData(userId, currentLayout.id, widget.id).data
                    } catch (e: Exception) {
                        logger.warn("Error al obtener datos del widget ${widget.id}: ${e.message}")
                        emptyMap()
                    }
                }
            } else {
                emptyMap()
            }
            
            // Crear metadata
            val metadata = DashboardMetadataDto(
                lastUpdated = LocalDateTime.now(),
                theme = preferences.dashboardTheme ?: "light",
                totalWidgets = currentLayout.widgets.size,
                totalLayouts = allLayouts.totalElements.toInt()
            )
            
            // Crear respuesta completa
            val completeDashboard = CompleteDashboardDto(
                user = user,
                preferences = preferences,
                currentLayout = currentLayout,
                availableLayouts = allLayouts.content,
                widgetsData = widgetsData,
                metadata = metadata
            )
            
            val response = DashboardApiResponseDto(
                success = true,
                data = completeDashboard,
                message = "Dashboard cargado exitosamente"
            )
            
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            logger.error("Error al obtener dashboard completo: ${e.message}", e)
            
            val response = DashboardApiResponseDto(
                success = false,
                data = null,
                message = "Error al cargar el dashboard",
                errors = listOf(e.message ?: "Error desconocido")
            )
            
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response)
        }
    }

    @PostMapping("/initialize")
    // @PreAuthorize("hasRole('USUARIO') or hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun initializeDashboard(
        @Valid @RequestBody initDto: DashboardInitDto
    ): ResponseEntity<DashboardApiResponseDto> {
        logger.info("Petición para inicializar dashboard para usuario: ${initDto.userId}")
        
        return try {
            // Crear preferencias por defecto si no existen
            try {
                dashboardService.getUserPreferences(initDto.userId)
            } catch (_: Exception) {
                dashboardService.createDefaultUserPreferences(initDto.userId)
            }
            
            // Obtener o crear layout por defecto
            val defaultLayout = dashboardService.getDefaultDashboardLayout(initDto.userId)
            
            // Obtener dashboard completo
            val completeDashboardResponse = getCompleteDashboard(
                userId = initDto.userId,
                layoutId = defaultLayout.id,
                includeWidgetData = initDto.includeWidgetData
            )
            
            val response = DashboardApiResponseDto(
                success = true,
                data = completeDashboardResponse.body?.data,
                message = "Dashboard inicializado exitosamente"
            )
            
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            logger.error("Error al inicializar dashboard: ${e.message}", e)
            
            val response = DashboardApiResponseDto(
                success = false,
                data = null,
                message = "Error al inicializar el dashboard",
                errors = listOf(e.message ?: "Error desconocido")
            )
            
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response)
        }
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun getDashboardStats(
        @RequestParam userId: UUID
    ): ResponseEntity<DashboardStatsDto> {
        logger.debug("Petición para obtener estadísticas del dashboard para usuario: {}", userId)
        
        return try {
            val layouts = dashboardService.getUserDashboardLayouts(userId, Pageable.unpaged())
            val totalWidgets = layouts.content.sumOf { it.widgets.size }
            val mostUsedWidgetType = layouts.content
                .flatMap { it.widgets }
                .groupingBy { it.widgetType.name }
                .eachCount()
                .maxByOrNull { it.value }?.key ?: "N/A"
            
            val stats = DashboardStatsDto(
                totalLayouts = layouts.totalElements.toInt(),
                totalWidgets = totalWidgets,
                mostUsedWidgetType = mostUsedWidgetType,
                averageWidgetsPerLayout = if (layouts.totalElements > 0) {
                    totalWidgets.toDouble() / layouts.totalElements
                } else 0.0,
                lastAccessDate = LocalDateTime.now(), // Esto se podría trackear realmente
                creationDate = layouts.content.minOfOrNull { it.createdAt ?: LocalDateTime.now() } ?: LocalDateTime.now()
            )
            
            ResponseEntity.ok(stats)
        } catch (e: Exception) {
            logger.error("Error al obtener estadísticas del dashboard: ${e.message}", e)
            throw e
        }
    }

    @PostMapping("/backup")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun createDashboardBackup(
        @RequestParam userId: UUID
    ): ResponseEntity<DashboardBackupDto> {
        logger.info("Petición para crear backup del dashboard para usuario: $userId")
        
        return try {
            val layouts = dashboardService.getUserDashboardLayouts(userId, Pageable.unpaged())
            val preferences = dashboardService.getUserPreferences(userId)
            
            val backup = DashboardBackupDto(
                backupDate = LocalDateTime.now(),
                userId = userId,
                layouts = layouts.content,
                preferences = preferences
            )
            
            ResponseEntity.ok(backup)
        } catch (e: Exception) {
            logger.error("Error al crear backup del dashboard: ${e.message}", e)
            throw e
        }
    }

    @PostMapping("/restore")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMINISTRADOR')")
    fun restoreDashboardBackup(
        @RequestParam userId: UUID,
        @RequestBody backup: DashboardBackupDto
    ): ResponseEntity<DashboardApiResponseDto> {
        logger.info("Petición para restaurar backup del dashboard para usuario: $userId")
        
        return try {
            // Validar que el backup es para el usuario correcto
            if (backup.userId != userId) {
                throw DashboardValidationException("El backup no pertenece al usuario especificado") as Throwable
            }
            
            // Restaurar preferencias
            dashboardService.updateUserPreferences(userId, UserPreferencesUpdateDto(
                dashboardTheme = backup.preferences.dashboardTheme,
                sidebarCollapsed = backup.preferences.sidebarCollapsed,
                // ... Otros campos según sea necesario
            ))
            
            // Restaurar layouts (esto podría ser más complejo en la implementación real)
            backup.layouts.map { layout ->
                val layoutDto = DashboardLayoutCreateDto(
                    name = "${layout.name} (Restaurado)",
                    description = layout.description,
                    gridColumns = layout.gridColumns,
                    gridRows = layout.gridRows,
                    theme = layout.theme
                )
                dashboardService.createDashboardLayout(userId, layoutDto)
            }
            
            val response = DashboardApiResponseDto(
                success = true,
                data = null,
                message = "Dashboard restaurado exitosamente"
            )
            
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            logger.error("Error al restaurar backup del dashboard: ${e.message}", e)
            
            val response = DashboardApiResponseDto(
                success = false,
                data = null,
                message = "Error al restaurar el dashboard",
                errors = listOf(e.message ?: "Error desconocido")
            )
            
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response)
        }
    }
}
