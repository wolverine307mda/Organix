package com.wolverine.organix.dashboard.services

import com.wolverine.organix.dashboard.dto.*
import com.wolverine.organix.dashboard.models.DashboardLayout
import com.wolverine.organix.dashboard.models.DashboardWidget
import com.wolverine.organix.dashboard.models.WidgetType
import com.wolverine.organix.dashboard.repositories.DashboardLayoutRepository
import com.wolverine.organix.dashboard.repositories.DashboardWidgetRepository
import com.wolverine.organix.dashboard.exceptions.*
import com.wolverine.organix.users.models.Usuario
import com.wolverine.organix.users.repositories.UsuarioRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Page
import java.util.*
import java.time.LocalDateTime

@Service
@Transactional
class DashboardService(
    private val dashboardLayoutRepository: DashboardLayoutRepository,
    private val dashboardWidgetRepository: DashboardWidgetRepository,
    private val usuarioRepository: UsuarioRepository,
    private val objectMapper: com.fasterxml.jackson.databind.ObjectMapper
) {

    // =============== HELPER METHODS ===============
    
    private fun mapToJson(map: Map<String, Any>?): String? {
        return map?.let { objectMapper.writeValueAsString(it) }
    }
    
    private fun jsonToMap(json: String?): Map<String, Any>? {
        return json?.let { 
            try {
                objectMapper.readValue(it, Map::class.java) as Map<String, Any>
            } catch (e: Exception) {
                null
            }
        }
    }

    // =============== LAYOUT METHODS ===============

    fun getUserDashboards(usuarioId: UUID): List<DashboardLayoutDto> {
        val layouts = dashboardLayoutRepository.findActiveLayoutsByUsuarioId(usuarioId)
        return layouts.map { convertToDto(it) }
    }

    fun getUserDefaultDashboard(usuarioId: UUID): DashboardLayoutDto? {
        val usuario = usuarioRepository.findById(usuarioId).orElseThrow {
            throw RuntimeException("Usuario no encontrado")
        }
        
        val defaultLayout = dashboardLayoutRepository.findByUsuarioAndIsDefaultTrueAndIsActiveTrue(usuario)
            ?: createDefaultDashboard(usuario)
            
        return convertToDto(defaultLayout)
    }

    fun createDashboard(usuarioId: UUID, request: DashboardLayoutCreateDto): DashboardLayoutDto {
        val usuario = usuarioRepository.findById(usuarioId).orElseThrow {
            throw RuntimeException("Usuario no encontrado")
        }

        // Si este será el dashboard por defecto, desactivar el anterior
        if (request.isDefault) {
            dashboardLayoutRepository.findByUsuarioAndIsDefaultTrueAndIsActiveTrue(usuario)?.let {
                it.isDefault = false
                dashboardLayoutRepository.save(it)
            }
        }

        val layout = DashboardLayout(
            usuario = usuario,
            name = request.name,
            description = request.description,
            isDefault = request.isDefault,
            gridColumns = request.gridColumns,
            gridRows = request.gridRows,
            backgroundColor = request.backgroundColor,
            backgroundImage = request.backgroundImage,
            theme = request.theme
        )

        val savedLayout = dashboardLayoutRepository.save(layout)
        
        // Crear widgets por defecto
        createDefaultWidgets(savedLayout)
        
        return convertToDto(savedLayout)
    }

    private fun createDefaultDashboard(usuario: Usuario): DashboardLayout {
        val layout = DashboardLayout(
            usuario = usuario,
            name = "Mi Dashboard",
            description = "Dashboard por defecto",
            isDefault = true,
            gridColumns = 12,
            gridRows = 8,
            backgroundColor = "#ffffff",
            backgroundImage = null,
            theme = "light"
        )

        val savedLayout = dashboardLayoutRepository.save(layout)
        createDefaultWidgets(savedLayout)
        
        return savedLayout
    }

    private fun createDefaultWidgets(layout: DashboardLayout) {
        val defaultWidgets = listOf(
            DashboardWidget(
                dashboardLayout = layout,
                type = WidgetType.WELCOME,
                title = "¡Bienvenido!",
                description = "Widget de bienvenida",
                x = 0, y = 0, width = 6, height = 2,
                orderIndex = 1
            ),
            DashboardWidget(
                dashboardLayout = layout,
                type = WidgetType.QUICK_STATS,
                title = "Estadísticas Rápidas",
                description = "Resumen de estadísticas",
                x = 6, y = 0, width = 6, height = 2,
                orderIndex = 2
            ),
            DashboardWidget(
                dashboardLayout = layout,
                type = WidgetType.CALENDAR,
                title = "Calendario",
                description = "Vista del calendario",
                x = 0, y = 2, width = 8, height = 4,
                orderIndex = 3
            ),
            DashboardWidget(
                dashboardLayout = layout,
                type = WidgetType.NOTES,
                title = "Notas Rápidas",
                description = "Tus notas importantes",
                x = 8, y = 2, width = 4, height = 4,
                orderIndex = 4
            )
        )

        dashboardWidgetRepository.saveAll(defaultWidgets)
    }

    /**
     * Convierte entidad a DTO
     */
    private fun convertToDto(layout: DashboardLayout): DashboardLayoutDto {
        return DashboardLayoutDto(
            id = layout.id,
            name = layout.name,
            description = layout.description,
            isDefault = layout.isDefault,
            isActive = layout.isActive,
            widgets = layout.widgets.map { convertToWidgetDto(it) },
            createdAt = layout.createdAt,
            updatedAt = layout.updatedAt
        )
    }

    /**
     * Convierte widget a DTO
     */
    private fun convertToWidgetDto(widget: DashboardWidget): DashboardWidgetDto {
        return DashboardWidgetDto(
            id = widget.id,
            type = widget.type,
            title = widget.title,
            description = widget.description,
            x = widget.x,
            y = widget.y,
            width = widget.width,
            height = widget.height,
            configuration = jsonToMap(widget.configuration),
            isVisible = widget.isVisible,
            isMinimized = widget.isMinimized,
            orderIndex = widget.orderIndex,
            createdAt = widget.createdAt,
            updatedAt = widget.updatedAt
        )
    }

    /**
     * Convierte entidad a DTO de respuesta
     */
    private fun convertToLayoutDto(layout: DashboardLayout): DashboardLayoutResponseDto {
        return DashboardLayoutResponseDto(
            id = layout.id,
            userId = layout.usuario.id,
            name = layout.name,
            description = layout.description,
            isDefault = layout.isDefault,
            gridColumns = layout.gridColumns,
            gridRows = layout.gridRows,
            backgroundColor = layout.backgroundColor,
            backgroundImage = layout.backgroundImage,
            theme = layout.theme,
            widgets = layout.widgets.map { convertToWidgetResponseDto(it) },
            createdAt = layout.createdAt,
            updatedAt = layout.updatedAt
        )
    }

    /**
     * Convierte widget a DTO de respuesta
     */
    private fun convertToWidgetResponseDto(widget: DashboardWidget): DashboardWidgetResponseDto {
        return DashboardWidgetResponseDto(
            id = widget.id,
            dashboardLayoutId = widget.dashboardLayout.id,
            widgetType = widget.type,
            title = widget.title,
            description = widget.description,
            xPosition = widget.x,
            yPosition = widget.y,
            width = widget.width,
            height = widget.height,
            isVisible = widget.isVisible,
            isMinimized = widget.isMinimized,
            zIndex = widget.orderIndex,
            widgetConfig = jsonToMap(widget.configuration),
            createdAt = widget.createdAt,
            updatedAt = widget.updatedAt
        )
    }

    /**
     * Obtiene un layout específico con sus widgets
     */
    fun getDashboardLayout(userId: UUID, layoutId: UUID): DashboardLayoutResponseDto {
        val layout = dashboardLayoutRepository.findByIdAndUserId(layoutId, userId)
            ?: throw DashboardNotFoundException("Layout no encontrado")
        
        val widgets = dashboardWidgetRepository.findByDashboardLayoutId(layoutId)
        
        return DashboardLayoutResponseDto(
            id = layout.id,
            userId = layout.usuario.id,
            name = layout.name,
            description = layout.description,
            isDefault = layout.isDefault,
            gridColumns = layout.gridColumns,
            gridRows = layout.gridRows,
            backgroundColor = layout.backgroundColor,
            backgroundImage = layout.backgroundImage,
            theme = layout.theme,
            widgets = widgets.map { convertToWidgetResponseDto(it) },
            createdAt = layout.createdAt,
            updatedAt = layout.updatedAt
        )
    }

    /**
     * Obtiene el layout por defecto del usuario
     */
    fun getDefaultDashboardLayout(userId: UUID): DashboardLayoutResponseDto {
        val layout = dashboardLayoutRepository.findByUserIdAndIsDefault(userId, true)
            ?: throw DashboardNotFoundException("Layout por defecto no encontrado")
        
        return getDashboardLayout(userId, layout.id)
    }

    /**
     * Actualiza un layout de dashboard
     */
    fun updateDashboardLayout(userId: UUID, layoutId: UUID, layoutDto: DashboardLayoutUpdateDto): DashboardLayoutResponseDto {
        val layout = dashboardLayoutRepository.findByIdAndUserId(layoutId, userId)
            ?: throw DashboardNotFoundException("Layout no encontrado")
        
        // Actualizar campos directamente
        layoutDto.name?.let { layout.name = it }
        layoutDto.description?.let { layout.description = it }
        layoutDto.isDefault?.let { layout.isDefault = it }
        layoutDto.gridColumns?.let { layout.gridColumns = it }
        layoutDto.gridRows?.let { layout.gridRows = it }
        layoutDto.backgroundColor?.let { layout.backgroundColor = it }
        layoutDto.backgroundImage?.let { layout.backgroundImage = it }
        layoutDto.theme?.let { layout.theme = it }

        dashboardLayoutRepository.save(layout)
        return getDashboardLayout(userId, layoutId)
    }

    /**
     * Elimina un layout de dashboard
     */
    fun deleteDashboardLayout(userId: UUID, layoutId: UUID): Boolean {
        val layout = dashboardLayoutRepository.findByIdAndUserId(layoutId, userId)
            ?: throw DashboardNotFoundException("Layout no encontrado")
        
        if (layout.isDefault) {
            throw DashboardValidationException("No se puede eliminar el layout por defecto")
        }
        
        dashboardLayoutRepository.delete(layout)
        return true
    }

    /**
     * Establece un layout como predeterminado
     */
    fun setDefaultDashboardLayout(userId: UUID, layoutId: UUID): DashboardLayoutResponseDto {
        // Quitar el default de todos los layouts del usuario
        val userLayouts = dashboardLayoutRepository.findByUserId(userId)
        userLayouts.forEach { layout ->
            if (layout.isDefault) {
                layout.isDefault = false
                dashboardLayoutRepository.save(layout)
            }
        }
        
        // Establecer el nuevo layout como default
        val targetLayout = dashboardLayoutRepository.findByIdAndUserId(layoutId, userId)
            ?: throw DashboardNotFoundException("Layout no encontrado")
        
        targetLayout.isDefault = true
        dashboardLayoutRepository.save(targetLayout)
        return getDashboardLayout(userId, layoutId)
    }

    /**
     * Busca layouts por término
     */
    fun searchDashboardLayouts(userId: UUID, searchTerm: String, pageable: Pageable): Page<DashboardLayoutResponseDto> {
        val layouts = dashboardLayoutRepository.findByUserIdAndNameContainingIgnoreCase(userId, searchTerm, pageable)
        
        return layouts.map { layout ->
            val widgets = dashboardWidgetRepository.findByDashboardLayoutId(layout.id)
            DashboardLayoutResponseDto(
                id = layout.id,
                userId = layout.usuario.id,
                name = layout.name,
                description = layout.description,
                isDefault = layout.isDefault,
                gridColumns = layout.gridColumns,
                gridRows = layout.gridRows,
                backgroundColor = layout.backgroundColor,
                backgroundImage = layout.backgroundImage,
                theme = layout.theme,
                widgets = widgets.map { convertToWidgetResponseDto(it) },
                createdAt = layout.createdAt,
                updatedAt = layout.updatedAt
            )
        }
    }

    /**
     * Duplica un layout existente
     */
    fun duplicateDashboardLayout(userId: UUID, layoutId: UUID, newName: String): DashboardLayoutResponseDto {
        val originalLayout = dashboardLayoutRepository.findByIdAndUserId(layoutId, userId)
            ?: throw DashboardNotFoundException("Layout no encontrado")
        
        val usuario = usuarioRepository.findById(userId).orElseThrow { 
            RuntimeException("Usuario no encontrado") 
        }
        
        val newLayout = DashboardLayout(
            usuario = usuario,
            name = newName,
            description = "${originalLayout.description} (Copia)",
            isDefault = false,
            gridColumns = originalLayout.gridColumns,
            gridRows = originalLayout.gridRows,
            backgroundColor = originalLayout.backgroundColor,
            backgroundImage = originalLayout.backgroundImage,
            theme = originalLayout.theme
        )
        
        val savedLayout = dashboardLayoutRepository.save(newLayout)
        
        // Duplicar widgets
        val originalWidgets = dashboardWidgetRepository.findByDashboardLayoutId(originalLayout.id)
        originalWidgets.forEach { widget ->
            val newWidget = DashboardWidget(
                dashboardLayout = savedLayout,
                type = widget.type,
                title = widget.title,
                description = widget.description,
                x = widget.x,
                y = widget.y,
                width = widget.width,
                height = widget.height,
                configuration = widget.configuration,
                isVisible = widget.isVisible,
                isMinimized = widget.isMinimized,
                orderIndex = widget.orderIndex
            )
            dashboardWidgetRepository.save(newWidget)
        }
        
        return getDashboardLayout(userId, savedLayout.id)
    }

    /**
     * Crea un widget en un layout
     */
    fun createWidget(userId: UUID, layoutId: UUID, widgetDto: DashboardWidgetRequestDto): DashboardWidgetResponseDto {
        val layout = dashboardLayoutRepository.findByIdAndUserId(layoutId, userId)
            ?: throw DashboardNotFoundException("Layout no encontrado")
        
        val widget = DashboardWidget(
            dashboardLayout = layout,
            type = widgetDto.widgetType,
            title = widgetDto.title,
            description = widgetDto.description,
            x = widgetDto.xPosition,
            y = widgetDto.yPosition,
            width = widgetDto.width,
            height = widgetDto.height,
            configuration = mapToJson(widgetDto.widgetConfig),
            isVisible = widgetDto.isVisible,
            isMinimized = widgetDto.isMinimized,
            orderIndex = widgetDto.zIndex
        )
        
        val savedWidget = dashboardWidgetRepository.save(widget)
        return convertToWidgetResponseDto(savedWidget)
    }

    /**
     * Obtiene todos los widgets de un layout
     */
    fun getLayoutWidgets(userId: UUID, layoutId: UUID): List<DashboardWidgetResponseDto> {
        val layout = dashboardLayoutRepository.findByIdAndUserId(layoutId, userId)
            ?: throw DashboardNotFoundException("Layout no encontrado")
        
        val widgets = dashboardWidgetRepository.findByDashboardLayoutId(layoutId)
        return widgets.map { convertToWidgetResponseDto(it) }
    }

    /**
     * Obtiene un widget específico
     */
    fun getWidget(userId: UUID, layoutId: UUID, widgetId: UUID): DashboardWidgetResponseDto {
        val layout = dashboardLayoutRepository.findByIdAndUserId(layoutId, userId)
            ?: throw DashboardNotFoundException("Layout no encontrado")
        
        val widget = dashboardWidgetRepository.findByIdAndDashboardLayoutId(widgetId, layoutId)
            ?: throw WidgetNotFoundException("Widget no encontrado")
        
        return convertToWidgetResponseDto(widget)
    }

    /**
     * Actualiza un widget
     */
    fun updateWidget(userId: UUID, layoutId: UUID, widgetId: UUID, widgetDto: DashboardWidgetUpdateDto): DashboardWidgetResponseDto {
        val layout = dashboardLayoutRepository.findByIdAndUserId(layoutId, userId)
            ?: throw DashboardNotFoundException("Layout no encontrado")
        
        val widget = dashboardWidgetRepository.findByIdAndDashboardLayoutId(widgetId, layoutId)
            ?: throw WidgetNotFoundException("Widget no encontrado")
        
        // Actualizar campos directamente
        widgetDto.title?.let { widget.title = it }
        widgetDto.description?.let { widget.description = it }
        widgetDto.xPosition?.let { widget.x = it }
        widgetDto.yPosition?.let { widget.y = it }
        widgetDto.width?.let { widget.width = it }
        widgetDto.height?.let { widget.height = it }
        widgetDto.isVisible?.let { widget.isVisible = it }
        widgetDto.isMinimized?.let { widget.isMinimized = it }
        widgetDto.zIndex?.let { widget.orderIndex = it }
        widgetDto.widgetConfig?.let { widget.configuration = mapToJson(it) }
        
        val savedWidget = dashboardWidgetRepository.save(widget)
        return convertToWidgetResponseDto(savedWidget)
    }

    /**
     * Elimina un widget
     */
    fun deleteWidget(userId: UUID, layoutId: UUID, widgetId: UUID): Boolean {
        val layout = dashboardLayoutRepository.findByIdAndUserId(layoutId, userId)
            ?: throw DashboardNotFoundException("Layout no encontrado")
        
        val widget = dashboardWidgetRepository.findByIdAndDashboardLayoutId(widgetId, layoutId)
            ?: throw WidgetNotFoundException("Widget no encontrado")
        
        dashboardWidgetRepository.delete(widget)
        return true
    }

    /**
     * Actualiza las posiciones de múltiples widgets
     */
    fun updateWidgetPositions(userId: UUID, layoutId: UUID, positions: Map<UUID, DashboardWidgetPositionDto>): List<DashboardWidgetResponseDto> {
        val layout = dashboardLayoutRepository.findByIdAndUserId(layoutId, userId)
            ?: throw DashboardNotFoundException("Layout no encontrado")
        
        val updatedWidgets = mutableListOf<DashboardWidgetResponseDto>()
        
        positions.forEach { (widgetId, position) ->
            val widget = dashboardWidgetRepository.findByIdAndDashboardLayoutId(widgetId, layoutId)
                ?: throw WidgetNotFoundException("Widget no encontrado")
            
            widget.x = position.xPosition
            widget.y = position.yPosition
            position.width?.let { widget.width = it }
            position.height?.let { widget.height = it }
            
            val savedWidget = dashboardWidgetRepository.save(widget)
            updatedWidgets.add(convertToWidgetResponseDto(savedWidget))
        }
        
        return updatedWidgets
    }

    // =============== MISSING METHODS REQUIRED BY CONTROLLER ===============

    fun createDashboardLayout(userId: UUID, layoutDto: DashboardLayoutCreateDto): DashboardLayoutResponseDto {
        val usuario = usuarioRepository.findById(userId).orElseThrow {
            RuntimeException("Usuario no encontrado")
        }

        val layout = DashboardLayout(
            usuario = usuario,
            name = layoutDto.name,
            description = layoutDto.description,
            isDefault = layoutDto.isDefault,
            gridColumns = layoutDto.gridColumns,
            gridRows = layoutDto.gridRows,
            theme = layoutDto.theme,
            backgroundColor = layoutDto.backgroundColor,
            backgroundImage = layoutDto.backgroundImage,
            isDeleted = false
        )

        val savedLayout = dashboardLayoutRepository.save(layout)
        return convertToLayoutDto(savedLayout)
    }

    fun getUserDashboardLayouts(userId: UUID, pageable: Pageable): Page<DashboardLayoutResponseDto> {
        val layouts = dashboardLayoutRepository.findByUserIdAndIsDeletedFalse(userId, pageable)
        return layouts.map { convertToLayoutDto(it) }
    }

    fun updateWidgetPositions(userId: UUID, layoutId: UUID, positions: List<DashboardWidgetPositionUpdateDto>): List<DashboardWidgetResponseDto> {
        val layout = dashboardLayoutRepository.findByIdAndUserId(layoutId, userId)
            ?: throw DashboardNotFoundException("Layout no encontrado")
        
        val updatedWidgets = mutableListOf<DashboardWidgetResponseDto>()
        
        positions.forEach { position ->
            val widget = dashboardWidgetRepository.findByIdAndDashboardLayoutId(position.widgetId, layoutId)
                ?: throw WidgetNotFoundException("Widget no encontrado")
            
            widget.x = position.x
            widget.y = position.y
            widget.width = position.width
            widget.height = position.height
            
            val savedWidget = dashboardWidgetRepository.save(widget)
            updatedWidgets.add(convertToWidgetResponseDto(savedWidget))
        }
        
        return updatedWidgets
    }

    fun getWidgetsByType(userId: UUID, widgetType: WidgetType): List<DashboardWidgetResponseDto> {
        val userLayouts = dashboardLayoutRepository.findByUserIdAndIsDeletedFalse(userId)
        val widgets = mutableListOf<DashboardWidget>()
        
        userLayouts.forEach { layout ->
            val layoutWidgets = dashboardWidgetRepository.findByDashboardLayoutIdAndWidgetType(layout.id, widgetType)
            widgets.addAll(layoutWidgets)
        }
        
        return widgets.map { convertToWidgetResponseDto(it) }
    }

    fun getWidgetData(userId: UUID, layoutId: UUID, widgetId: UUID): DashboardWidgetDataDto {
        val widget = getWidget(userId, layoutId, widgetId)
        
        // Implementar lógica específica según el tipo de widget
        return DashboardWidgetDataDto(
            widgetId = widget.id,
            widgetType = widget.widgetType,
            data = mapOf(
                "title" to widget.title,
                "description" to (widget.description ?: ""),
                "lastUpdated" to LocalDateTime.now().toString()
            )
        )
    }

    fun getDashboardData(userId: UUID, layoutId: UUID): Map<String, Any> {
        val layout = getDashboardLayout(userId, layoutId)
        val widgets = getLayoutWidgets(userId, layoutId)
        
        return mapOf(
            "layout" to layout,
            "widgets" to widgets,
            "totalWidgets" to widgets.size,
            "lastUpdated" to LocalDateTime.now().toString()
        )
    }

    fun getUserPreferences(userId: UUID): UserPreferencesResponseDto {
        val usuario = usuarioRepository.findById(userId).orElseThrow {
            RuntimeException("Usuario no encontrado")
        }
        
        // Por ahora devolver preferencias por defecto
        return UserPreferencesResponseDto(
            userId = userId,
            dashboardTheme = "light",
            sidebarCollapsed = false,
            language = "es",
            timezone = "America/Mexico_City",
            notifications = true,
            autoSave = true,
            gridColumns = 12,
            gridRows = 8
        )
    }

    fun updateUserPreferences(userId: UUID, preferencesDto: UserPreferencesUpdateDto): UserPreferencesResponseDto {
        val usuario = usuarioRepository.findById(userId).orElseThrow {
            RuntimeException("Usuario no encontrado")
        }
        
        // Aquí actualizarías las preferencias del usuario
        // Por ahora solo devolver las actualizadas
        return UserPreferencesResponseDto(
            userId = userId,
            dashboardTheme = preferencesDto.dashboardTheme ?: "light",
            sidebarCollapsed = preferencesDto.sidebarCollapsed ?: false,
            language = "es",
            timezone = "America/Mexico_City",
            notifications = preferencesDto.notifications ?: true,
            autoSave = preferencesDto.autoSave ?: true,
            gridColumns = preferencesDto.gridColumns ?: 12,
            gridRows = preferencesDto.gridRows ?: 8
        )
    }

    fun createDefaultUserPreferences(userId: UUID): UserPreferencesResponseDto {
        val usuario = usuarioRepository.findById(userId).orElseThrow {
            RuntimeException("Usuario no encontrado")
        }
        
        return UserPreferencesResponseDto(
            userId = userId,
            dashboardTheme = "light",
            sidebarCollapsed = false,
            language = "es",
            timezone = "America/Mexico_City",
            notifications = true,
            autoSave = true,
            gridColumns = 12,
            gridRows = 8
        )
    }

    fun exportDashboardConfig(userId: UUID, layoutId: UUID): Map<String, Any> {
        val layout = getDashboardLayout(userId, layoutId)
        val widgets = getLayoutWidgets(userId, layoutId)
        
        return mapOf(
            "layout" to layout,
            "widgets" to widgets,
            "exportDate" to LocalDateTime.now().toString(),
            "version" to "1.0"
        )
    }

    fun importDashboardConfig(userId: UUID, config: Map<String, Any>): DashboardLayoutResponseDto {
        val usuario = usuarioRepository.findById(userId).orElseThrow {
            RuntimeException("Usuario no encontrado")
        }
        
        // Crear un nuevo layout basado en la configuración importada
        val layoutData = config["layout"] as? Map<String, Any>
            ?: throw IllegalArgumentException("Configuración de layout inválida")
        
        val layout = DashboardLayout(
            usuario = usuario,
            name = "${layoutData["name"] as? String ?: "Imported Layout"} (Imported)",
            description = layoutData["description"] as? String ?: "Layout importado",
            isDefault = false,
            gridColumns = layoutData["gridColumns"] as? Int ?: 12,
            gridRows = layoutData["gridRows"] as? Int ?: 8,
            theme = layoutData["theme"] as? String ?: "light",
            backgroundColor = layoutData["backgroundColor"] as? String,
            backgroundImage = layoutData["backgroundImage"] as? String,
            isDeleted = false
        )
        
        val savedLayout = dashboardLayoutRepository.save(layout)
        return convertToLayoutDto(savedLayout)
    }

    // =============== END MISSING METHODS ===============

    fun findByUserIdAndNameContainingIgnoreCase(userId: UUID, searchTerm: String, pageable: Pageable): Page<DashboardLayout> {
        throw NotImplementedError("Método no implementado")
    }
}
