package builder

interface ReportBuilder {

    fun addHeader()

    fun addStatistics(
        total: Int,
        completed: Int
    )

    fun addTasks(tasks: String)

    fun build(): String
}