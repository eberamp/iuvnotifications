package mx.edu.iuv.monitor.domain.model

import java.util.Date

data class IuvUser (
    val id: Long,
    val firstName: String,
    val email: String?,
    val phoneNumber: String? = null,
    val lastAccess: Date?,
    val courses: List<UserCourse?>
)