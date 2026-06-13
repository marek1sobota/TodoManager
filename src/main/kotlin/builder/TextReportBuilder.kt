package builder

class TextReportBuilder : ReportBuilder {

    private val report = StringBuilder()

    override fun addHeader() {

        report.appendLine(
            "===== TODO MANAGER REPORT ====="
        )

        report.appendLine()
    }

    override fun addStatistics(
        total: Int,
        completed: Int
    ) {

        report.appendLine(
            "Total tasks: $total"
        )

        report.appendLine(
            "Completed tasks: $completed"
        )

        report.appendLine(
            "Remaining tasks: ${total - completed}"
        )

        report.appendLine()
    }

    override fun addTasks(
        tasks: String
    ) {

        report.appendLine("Tasks:")

        report.appendLine(tasks)

        report.appendLine()
    }

    override fun build(): String {

        return report.toString()
    }
}