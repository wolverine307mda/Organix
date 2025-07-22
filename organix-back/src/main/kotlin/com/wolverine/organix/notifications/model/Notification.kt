package com.wolverine.organix.notifications.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "notifications")
data class Notification(
    @Id
    @Column(columnDefinition = "uuid")
    val id: UUID,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Column(nullable = false)
    val type: String,

    @Column(columnDefinition = "text", nullable = false)
    val message: String,

    @Column(nullable = false)
    val read: Boolean = false,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime
)
