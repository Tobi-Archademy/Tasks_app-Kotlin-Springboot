package org.oluwatobi.task_app.service

import org.oluwatobi.task_app.data.Task
import org.oluwatobi.task_app.data.model.TaskCreateRequest
import org.oluwatobi.task_app.data.model.TaskDto
import org.oluwatobi.task_app.data.model.TaskUpdateRequest
import org.oluwatobi.task_app.exception.BadRequestException
import org.oluwatobi.task_app.exception.TaskNotFoundException
import org.oluwatobi.task_app.repository.TaskRepository
import org.springframework.stereotype.Service
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field
import java.util.stream.Collectors
import kotlin.reflect.full.memberProperties

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

    fun createTask(request: TaskCreateRequest): TaskDto {
        if (taskRepository.doesDescriptionExist(request.description)) {
            throw BadRequestException("There is already a task with the description: ${request.description}")
        }

        val task = Task()
        mappingFromRequestToEntity(task, request)
        val savedTask: Task = taskRepository.save(task)
        return mappingEntityToDto(savedTask)
    }

    fun updateTask(id: Long, request: TaskUpdateRequest): TaskDto {
        checkTaskForId(id)
        val existingTask: Task = taskRepository.findTaskById(id)

        for (prop in TaskUpdateRequest::class.memberProperties) {
            if (prop.get(request) != null) {
                val field: Field? = ReflectionUtils.findField(Task::class.java, prop.name)
                field?.let {
                    it.isAccessible = true
                    ReflectionUtils.setField(it, existingTask, prop.get(request))
                }
            }
        }

        val savedTask: Task = taskRepository.save(existingTask)
        return mappingEntityToDto(savedTask)
    }

    fun deleteTask(id: Long): String {
        checkTaskForId(id)
        taskRepository.deleteById(id)
        return "Task with the ID: $id has been deleted."
    }

}