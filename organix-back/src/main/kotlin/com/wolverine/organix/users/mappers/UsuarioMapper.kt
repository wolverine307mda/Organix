package com.wolverine.organix.users.mappers

import com.wolverine.organix.users.dto.UsuarioCreateDto
import com.wolverine.organix.users.dto.UsuarioResponseDto
import com.wolverine.organix.users.dto.UsuarioUpdateDto
import com.wolverine.organix.users.models.Usuario
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class UsuarioMapper {

    private val logger = LoggerFactory.getLogger(UsuarioMapper::class.java)

    fun toEntity(dto: UsuarioCreateDto): Usuario {
        logger.debug("Mapeando UsuarioCreateDto a Usuario entity para email: ${dto.email}")
        
        return Usuario(
            email = dto.email,
            usernameField = dto.username,
            firstName = dto.firstName,
            lastName = dto.lastName,
            password = dto.password, // Se debe hashear en el servicio
            birthDate = dto.birthDate,
            addressLine = dto.addressLine,
            city = dto.city,
            postalCode = dto.postalCode,
            country = dto.country,
            phone = dto.phone,
            avatarUrl = dto.avatarUrl,
            language = dto.language,
            timezone = dto.timezone,
            rol = dto.rol
        )
    }

    fun toResponseDto(entity: Usuario): UsuarioResponseDto {
        logger.debug("Mapeando Usuario entity a UsuarioResponseDto para usuario: ${entity.id}")
        
        return UsuarioResponseDto(
            id = entity.id,
            email = entity.email,
            username = entity.getNombreUsuario(),
            firstName = entity.firstName,
            lastName = entity.lastName,
            birthDate = entity.birthDate,
            addressLine = entity.addressLine,
            city = entity.city,
            postalCode = entity.postalCode,
            country = entity.country,
            phone = entity.phone,
            avatarUrl = entity.avatarUrl,
            language = entity.language,
            timezone = entity.timezone,
            rol = entity.rol,
            lastLoginAt = entity.lastLoginAt,
            googleDriveLinked = entity.googleDriveLinked,
            dashboardLayoutId = entity.dashboardLayoutId,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    fun toResponseDtoList(entities: List<Usuario>): List<UsuarioResponseDto> {
        logger.debug("Mapeando lista de ${entities.size} usuarios a DTOs")
        return entities.map { toResponseDto(it) }
    }

    fun updateEntityFromDto(entity: Usuario, dto: UsuarioUpdateDto): Usuario {
        logger.debug("Actualizando Usuario entity con UsuarioUpdateDto para usuario: ${entity.id}")
        
        dto.email?.let { entity.email = it }
        dto.firstName?.let { entity.firstName = it }
        dto.lastName?.let { entity.lastName = it }
        dto.birthDate?.let { entity.birthDate = it }
        dto.addressLine?.let { entity.addressLine = it }
        dto.city?.let { entity.city = it }
        dto.postalCode?.let { entity.postalCode = it }
        dto.country?.let { entity.country = it }
        dto.phone?.let { entity.phone = it }
        dto.avatarUrl?.let { entity.avatarUrl = it }
        dto.language?.let { entity.language = it }
        dto.timezone?.let { entity.timezone = it }
        dto.googleDriveLinked?.let { entity.googleDriveLinked = it }
        dto.dashboardLayoutId?.let { entity.dashboardLayoutId = it }
        
        return entity
    }
}
