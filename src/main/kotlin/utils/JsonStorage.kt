package utils

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.Task
import java.io.File

object JsonStorage {

    private val file =
        File("tasks.json")

    private val json =
        Json {
            prettyPrint = true
            encodeDefaults = true
            ignoreUnknownKeys = true
        }

    fun save(tasks: List<Task>) {

        file.writeText(
            json.encodeToString(tasks)
        )
    }

    fun load(): List<Task> {

        if (!file.exists())
            return emptyList()

        return try {

            json.decodeFromString<List<Task>>(
                file.readText()
            )

        } catch (e: Exception) {

            emptyList()
        }
    }
}
