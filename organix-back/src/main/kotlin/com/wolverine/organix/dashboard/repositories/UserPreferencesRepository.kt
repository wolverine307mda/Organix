package com.wolverine.organix.dashboard.repositories

import com.wolverine.organix.dashboard.models.UserPreferences
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserPreferencesRepository : JpaRepository<UserPreferences, UUID> {

    fun findByUserId(userId: UUID): Optional<UserPreferences>

    fun existsByUserId(userId: UUID): Boolean

    fun deleteByUserId(userId: UUID): Int
}
