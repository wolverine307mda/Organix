package com.wolverine.organix.documents.model

import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "photo_memories")
data class PhotoMemory(
    @Id
    @Column(columnDefinition = "uuid")
    val id: UUID,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    val fileRef: FileRef,

    @Column(name = "taken_at", nullable = false)
    val takenAt: LocalDate,

    @Column
    val location: String?,

    @ElementCollection
    @CollectionTable(name = "photo_memory_tags", joinColumns = [JoinColumn(name = "photo_memory_id")])
    @Column(name = "tag")
    val tags: List<String> = mutableListOf()
)