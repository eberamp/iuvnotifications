package mx.edu.iuv.monitor.infrastructure.output.database.aggregations

import org.springframework.data.annotation.Id

data class UserReasonCount (

    @Id
    val userId: String,

    val reason: String,
    val count: Long

)