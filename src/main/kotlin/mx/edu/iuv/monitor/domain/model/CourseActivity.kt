package mx.edu.iuv.monitor.domain.model

import java.util.*

data class CourseActivity (
    val itemId: Long,
    val itemName: String = "",
    val courseId: Long = 0L,
    val gradeId: Long = 0L,
    val gradeValue: Long = 0L,
    val studentId: Long = 0L,
    val studentName: String = ""
)
