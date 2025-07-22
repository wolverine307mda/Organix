package com.wolverine.organix.documents.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "documents")
data class Document(
    @Id
    @Column(columnDefinition = "uuid")
    val id: UUID,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Column(nullable = false)
    val title: String,

    @Column(columnDefinition = "text", name = "content_html", nullable = false)
    val contentHtml: String,

    @Column(name = "export_options")
    val exportOptions: String?,

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    val attachments: List<FileRef> = mutableListOf(),

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime,

    @Column(name = "is_deleted", nullable = false)
    val isDeleted: Boolean = false
)