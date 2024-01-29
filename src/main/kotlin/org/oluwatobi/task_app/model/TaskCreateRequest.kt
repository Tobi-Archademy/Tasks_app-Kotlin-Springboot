package org.oluwatobi.task_app.model

import jakarta.persistence.Column
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.oluwatobi.task_app.data.Priority
import java.time.LocalDateTime

data class TaskCreateRequest(
    @NotBlank(message = "Task id can't be empty")
    val id: Long,

    @NotNull(message = "Task description can't be empty")
    val description: String,

    val isReminderSet: Boolean,

    val isTaskOpen: Boolean,

    @NotNull(message = "Task created_at can't be empty")
    val createdAt: LocalDateTime,

    val priority: Priority
)
