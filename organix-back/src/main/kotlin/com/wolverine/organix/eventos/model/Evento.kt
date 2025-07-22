package com.wolverine.organix.eventos.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*
import com.wolverine.organix.documents.model.FileRef

@Entity
@Table(name = "eventos")
data class Evento(
    @Id
    @Column(columnDefinition = "uuid")
    val id: UUID,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Column(nullable = false)
    val title: String,

    @Column(columnDefinition = "text")
    val description: String?,

    @Column(name = "start_date_time", nullable = false)
    val startDateTime: LocalDateTime,

    @Column(name = "end_date_time", nullable = false)
    val endDateTime: LocalDateTime,

    @Column
    val location: String?,

    @Column
    val recurrence: String?,

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id")
    val attachments: List<FileRef> = mutableListOf(),

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime,

    @Column(name = "is_deleted", nullable = false)
    val isDeleted: Boolean = false
)
