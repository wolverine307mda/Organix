package com.wolverine.organix.dashboard.mappers

import com.fasterxml.jackson.databind.ObjectMapper
import com.wolverine.organix.dashboard.dto.*
import com.wolverine.organix.dashboard.models.DashboardLayout
import com.wolverine.organix.dashboard.models.DashboardWidget
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

@Component
class DashboardWidgetMapper {

    private val logger = LoggerFactory.getLogger(DashboardWidgetMapper::class.java)
    private val objectMapper = ObjectMapper()

    fun toEntity(dto: DashboardWidgetRequestDto, layout: DashboardLayout): DashboardWidget {
        logger.debug("Mapeando DashboardWidgetRequestDto a DashboardWidget entity para layout: ${layout.id}")

        return DashboardWidget(
            dashboardLayout = layout,
            type = dto.widgetType,
            title = dto.title,
            description = dto.description,
            x = dto.xPosition,
            y = dto.yPosition,
            width = dto.width,
            height = dto.height,
            configuration = dto.widgetConfig?.let { objectMapper.writeValueAsString(it) },
            isVisible = dto.isVisible,
            isMinimized = dto.isMinimized,
            orderIndex = dto.zIndex
        )
    }

    fun toResponseDto(entity: DashboardWidget): DashboardWidgetResponseDto {
        logger.debug("Mapeando DashboardWidget entity a DashboardWidgetResponseDto: ${entity.id}")

        return DashboardWidgetResponseDto(
            id = entity.id,
            type = entity.type,
            title = entity.title,
            description = entity.description,
            x = entity.x,
            y = entity.y,
            width = entity.width,
            height = entity.height,
            configuration = entity.configuration?.let { 
                try {
                    objectMapper.readValue(it, Map::class.java) as Map<String, Any>
                } catch (e: Exception) {
                    logger.warn("Error parsing widget configuration JSON: ${e.message}")
                    emptyMap()
                }
            },
            isVisible = entity.isVisible,
            isMinimized = entity.isMinimized,
            orderIndex = entity.orderIndex,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    fun updateEntity(entity: DashboardWidget, dto: DashboardWidgetUpdateDto): DashboardWidget {
        logger.debug("Actualizando DashboardWidget entity: ${entity.id}")
        
        dto.title?.let { entity.title = it }
        dto.description?.let { entity.description = it }
        dto.x?.let { entity.x = it }
        dto.y?.let { entity.y = it }
        dto.width?.let { entity.width = it }
        dto.height?.let { entity.height = it }
        dto.configuration?.let { entity.configuration = objectMapper.writeValueAsString(it) }
        dto.isVisible?.let { entity.isVisible = it }
        dto.isMinimized?.let { entity.isMinimized = it }
        dto.orderIndex?.let { entity.orderIndex = it }
        
        return entity
    }

    fun toEntity(dto: DashboardWidgetRequestDto, layoutId: UUID): DashboardWidget {
        logger.debug("Mapeando DashboardWidgetRequestDto a DashboardWidget entity para layout: ${layoutId}")

        return DashboardWidget(
            dashboardLayout = DashboardLayout(id = layoutId),
            type = dto.widgetType,
            title = dto.title,
            description = dto.description,
            x = dto.xPosition,
            y = dto.yPosition,
            width = dto.width,
            height = dto.height,
            configuration = dto.widgetConfig?.let { objectMapper.writeValueAsString(it) },
            isVisible = dto.isVisible,
            isMinimized = dto.isMinimized,
            orderIndex = dto.zIndex
        )
    }
}
