package org.oluwatobi.task_app.exception

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
data class TaskNotFoundException(
    override val message: String
): RuntimeException()