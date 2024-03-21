package mx.edu.iuv.monitor.domain.model

import java.util.Date

data class UserCourse (
    val id: Long,
    val shortName: String = "",
    val url: String? = null,
    val isUserActive: Boolean,
    val lastActivityOn: Date
)
