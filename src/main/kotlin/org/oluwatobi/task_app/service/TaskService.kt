package org.oluwatobi.task_app.service

import org.oluwatobi.task_app.data.Task
import org.oluwatobi.task_app.data.model.TaskCreateRequest
import org.oluwatobi.task_app.data.model.TaskDto
import org.oluwatobi.task_app.exception.TaskNotFoundException
import org.oluwatobi.task_app.repository.TaskRepository
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class TaskService(private val taskRepository: TaskRepository) {

    private fun mappingEntityToDto(task: Task) =
        TaskDto(
        task.id,
        task.description,
        task.isReminderSet,
        task.isTaskOpen,
        task.createdAt,
        task.priority
    )

    private fun mappingFromRequestToEntity(task: Task, request: TaskCreateRequest) {
        task.description = request.description
        task.isReminderSet = request.isReminderSet
        task.isTaskOpen = request.isTaskOpen
        task.priority = request.priority
    }

    private fun checkTaskForId(id: Long)  {
        if (!taskRepository.existsById(id)) {
            throw TaskNotFoundException("Task with the ID: $id does not exist")
        }
    }

    fun getTaskById(id: Long): TaskDto {
        checkTaskForId(id)
        val task: Task = taskRepository.findTaskById(id)
        return mappingEntityToDto(task)
    }

    fun getAllTasks(): List<TaskDto> =
        taskRepository.findAll().stream().map(this::mappingEntityToDto).collect(Collectors.toList())

    fun getAllOpenTasks(): List<TaskDto> =
        taskRepository.queryAllOpenTasks().stream().map(this::mappingEntityToDto).collect(Collectors.toList())

    fun getAllClosedTasks(): List<TaskDto> =
        taskRepository.queryAllClosedTasks().stream().map(this::mappingEntityToDto).collect(Collectors.toList())
}