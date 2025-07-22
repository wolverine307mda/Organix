package com.wolverine.organix.tasks.model

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "tasks")
data class Task(
    @Id
    @Column(columnDefinition = "uuid")
    val id: UUID,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Column(nullable = false)
    val title: String,

    @Column(columnDefinition = "text")
    val description: String?,

    @Column(name = "due_date")
    val dueDate: LocalDate?,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val priority: TaskPriority,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: TaskStatus,

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    val subtasks: List<Subtask> = mutableListOf(),

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime,

    @Column(name = "is_deleted", nullable = false)
    val isDeleted: Boolean = false
)