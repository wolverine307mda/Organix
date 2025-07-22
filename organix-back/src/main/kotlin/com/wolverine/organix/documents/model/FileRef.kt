package com.wolverine.organix.documents.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "file_refs")
data class FileRef(
    @Id
    @Column(columnDefinition = "uuid")
    val id: UUID,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Column(nullable = false)
    val filename: String,

    @Column(nullable = false)
    val path: String,

    @Column(name = "mime_type", nullable = false)
    val mimeType: String,

    @Column(nullable = false)
    val size: Long,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime
)
