package com.wolverine.organix.notes.model

import com.wolverine.organix.documents.model.FileRef
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "notes")
data class Note(
    @Id
    @Column(columnDefinition = "uuid")
    val id: UUID,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Column(nullable = false)
    val title: String,

    @Column(columnDefinition = "text", name = "content_markdown", nullable = false)
    val contentMarkdown: String,

    @Column(columnDefinition = "text", name = "content_html", nullable = false)
    val contentHtml: String,

    @ElementCollection
    @CollectionTable(name = "note_tags", joinColumns = [JoinColumn(name = "note_id")])
    @Column(name = "tag")
    val tags: List<String> = mutableListOf(),

    @Column(name = "folder_id")
    val folderId: UUID?,

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id")
    val attachments: List<FileRef> = mutableListOf(),

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime,

    @Column(name = "is_deleted", nullable = false)
    val isDeleted: Boolean = false
)