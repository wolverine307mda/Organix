package com.wolverine.organix.dashboard.mappers

import com.wolverine.organix.dashboard.dto.*
import com.wolverine.organix.dashboard.models.DashboardLayout
import com.wolverine.organix.users.models.Usuario

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class DashboardLayoutMapper {

    private val logger = LoggerFactory.getLogger(DashboardLayoutMapper::class.java)

    fun toEntity(dto: DashboardLayoutCreateDto, usuario: Usuario): DashboardLayout {
        logger.debug("Mapeando DashboardLayoutCreateDto a DashboardLayout entity para usuario: ${usuario.id}")

        return DashboardLayout(
            usuario = usuario,
            name = dto.name,
            description = dto.description,
            isDefault = dto.isDefault,
            gridColumns = dto.gridColumns,
            gridRows = dto.gridRows,
            backgroundColor = dto.backgroundColor,
            backgroundImage = dto.backgroundImage,
            theme = dto.theme
        )
    }

    fun toResponseDto(entity: DashboardLayout, widgets: List<DashboardWidgetResponseDto> = emptyList()): DashboardLayoutResponseDto {
        logger.debug("Mapeando DashboardLayout entity a DashboardLayoutResponseDto para layout: ${entity.id}")
        
        return DashboardLayoutResponseDto(
            id = entity.id,
            userId = entity.usuario.id,
            name = entity.name,
            description = entity.description,
            isDefault = entity.isDefault,
            gridColumns = entity.gridColumns,
            gridRows = entity.gridRows,
            backgroundColor = entity.backgroundColor,
            backgroundImage = entity.backgroundImage,
            theme = entity.theme,
            widgets = widgets,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    fun toResponseDtoList(entities: List<DashboardLayout>, widgetsMap: Map<java.util.UUID, List<DashboardWidgetResponseDto>> = emptyMap()): List<DashboardLayoutResponseDto> {
        logger.debug("Mapeando lista de ${entities.size} layouts a DTOs")
        return entities.map { layout ->
            val widgets = widgetsMap[layout.id] ?: emptyList()
            toResponseDto(layout, widgets)
        }
    }

    fun updateEntityFromDto(entity: DashboardLayout, dto: DashboardLayoutUpdateDto): DashboardLayout {
        logger.debug("Actualizando DashboardLayout entity con DashboardLayoutUpdateDto para layout: ${entity.id}")
        
        return entity.copy(
            name = dto.name ?: entity.name,
            description = dto.description ?: entity.description,
            isDefault = dto.isDefault ?: entity.isDefault,
            gridColumns = dto.gridColumns ?: entity.gridColumns,
            gridRows = dto.gridRows ?: entity.gridRows,
            backgroundColor = dto.backgroundColor ?: entity.backgroundColor,
            backgroundImage = dto.backgroundImage ?: entity.backgroundImage,
            theme = dto.theme ?: entity.theme
        )
    }
}
