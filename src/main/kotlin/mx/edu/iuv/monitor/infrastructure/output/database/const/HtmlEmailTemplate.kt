package mx.edu.iuv.monitor.infrastructure.output.database.const

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "html-templates")
data class HtmlEmailTemplate (
    val teacher: TeacherTemplates,
    val student: StudentTemplates,
    val default: String,
) {
    data class TeacherTemplates (
        val courseInactive: String = "",
        val courseMissingWelcomeMessage: String = "",
        val courseActivitiesPendingGrading: String = ""
    )

    data class StudentTemplates (
        val courseInactive: String = "",
    )
}