package mx.edu.iuv.monitor.domain.model

import java.util.Date

data class IuvUser (
    val id: Long,
    val email: String,
    val phoneNumber: String,
    val lastActive: Date
)