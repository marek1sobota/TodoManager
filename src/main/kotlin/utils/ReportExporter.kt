package utils

import java.io.File

object ReportExporter {

    fun export(
        report: String
    ) {

        File(
            "report.txt"
        ).writeText(report)
    }
}