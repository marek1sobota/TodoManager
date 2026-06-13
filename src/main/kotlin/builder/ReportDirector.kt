package builder

import model.Task

class ReportDirector(

    private val builder: ReportBuilder

) {

    fun createReport(
        tasks: List<Task>
    ): String {

        val completed =
            tasks.count {
                it.completed
            }

        builder.addHeader()

        builder.addStatistics(
            tasks.size,
            completed
        )

        val taskText = tasks.joinToString("\n") {

            "[${it.priority}] ${it.title} " +
                    if (it.completed)
                        "(DONE)"
                    else
                        "(TODO)"
        }

        builder.addTasks(taskText)

        return builder.build()
    }
}