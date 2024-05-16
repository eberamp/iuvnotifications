package mx.edu.iuv.monitor.infrastructure.output.database.aggregations

import org.springframework.data.annotation.Id

data class ReasonCount(

    @Id
    val reason: String,
    val count: Long

)
