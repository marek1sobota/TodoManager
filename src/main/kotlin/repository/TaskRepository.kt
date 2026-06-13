package repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import model.Priority
import model.Task
import utils.JsonStorage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object TaskRepository {

    private var nextId = 1

    private val dateFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    private val repositoryScope =
        CoroutineScope(Dispatchers.IO)

    private val _tasks =
        MutableStateFlow<List<Task>>(emptyList())

    val tasks: StateFlow<List<Task>>
        get() = _tasks

    init {

        val loadedTasks =
            JsonStorage.load()

        _tasks.value = loadedTasks

        nextId =
            (loadedTasks.maxOfOrNull { it.id } ?: 0) + 1
    }

    fun addTask(
        title: String,
        priority: Priority
    ) {

        val task = Task(
            id = nextId++,
            title = title,
            priority = priority,
            createdAt = currentTimestamp()
        )

        _tasks.value += task

        saveTasks()
    }

    fun updateTask(
        taskId: Int,
        newTitle: String,
        newPriority: Priority
    ) {

        _tasks.value =
            _tasks.value.map { task ->

                if (task.id == taskId) {

                    task.copy(
                        title = newTitle,
                        priority = newPriority
                    )

                } else {

                    task
                }
            }

        saveTasks()
    }

    fun deleteTask(task: Task) {

        _tasks.value =
            _tasks.value.filterNot {
                it.id == task.id
            }

        saveTasks()
    }

    fun toggleTask(task: Task) {

        _tasks.value =
            _tasks.value.map {

                if (it.id == task.id)
                    it.copy(completed = !it.completed)
                else
                    it
            }

        saveTasks()
    }

    private fun saveTasks() {

        repositoryScope.launch {

            JsonStorage.save(
                _tasks.value
            )
        }
    }

    private fun currentTimestamp(): String =
        LocalDateTime.now().format(dateFormatter)
}
