package org.oluwatobi.task_app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TaskAppApplication

fun main(args: Array<String>) {
	runApplication<TaskAppApplication>(*args)
	println("Ekaaabo!!!!!!!!")
}
