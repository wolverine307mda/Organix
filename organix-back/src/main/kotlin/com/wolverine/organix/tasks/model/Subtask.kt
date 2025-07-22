package com.wolverine.organix.tasks.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "subtasks")
data class Subtask(
    @Id
    @Column(columnDefinition = "uuid")
    val id: UUID,

    @Column(name = "task_id", nullable = false)
    val taskId: UUID,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val done: Boolean = false
)