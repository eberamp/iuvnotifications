package mx.edu.iuv.monitor.domain.model

import java.util.Date

data class IuvUser (
    val id: Long,
    val firstName: String,
    val email: String?,
    val userRole: String,
    val phoneNumber: String? = null,
    val lastPlatformAccess: Date,
    val courses: List<UserCourse?>,
    val activities: List<CourseActivity?>
)