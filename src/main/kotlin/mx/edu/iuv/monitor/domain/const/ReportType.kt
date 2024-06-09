package mx.edu.iuv.monitor.domain.const

import java.util.EnumMap

enum class ReportType {

    TEACHER_ALL,
    TEACHER_COURSE_INACTIVITY,
    TEACHER_COURSE_MISSING_WELCOME_MESSAGE,
    TEACHER_COURSE_ACTIVITIES_PENDING_GRADING;

    companion object {
        private val map = entries.associateBy { it.name }
        infix fun from(name: String) = map[name]
    }

}