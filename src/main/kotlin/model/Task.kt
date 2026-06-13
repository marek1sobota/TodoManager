package model

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: Int,
    val title: String,
    val priority: Priority = Priority.MEDIUM,
    val completed: Boolean = false,
    val createdAt: String = ""
)
