package com.wolverine.organix.reminders.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "reminders")
data class Reminder(
    @Id
    @Column(columnDefinition = "uuid")
    val id: UUID,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Column(name = "target_type", nullable = false)
    val targetType: String,

    @Column(name = "target_id", nullable = false)
    val targetId: UUID,

    @Column(nullable = false)
    val datetime: LocalDateTime,

    @Column(nullable = false)
    val type: String,

    @Column(columnDefinition = "text")
    val message: String?
)