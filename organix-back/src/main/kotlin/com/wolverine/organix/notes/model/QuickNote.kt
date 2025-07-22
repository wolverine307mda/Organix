package com.wolverine.organix.notes.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "quick_notes")
data class QuickNote(
    @Id
    @Column(columnDefinition = "uuid")
    val id: UUID,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Column(columnDefinition = "text", nullable = false)
    val content: String,

    @Column(nullable = false)
    val color: String,

    @Column(columnDefinition = "text", nullable = false)
    val position: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime,

    @Column(name = "is_deleted", nullable = false)
    val isDeleted: Boolean = false
)