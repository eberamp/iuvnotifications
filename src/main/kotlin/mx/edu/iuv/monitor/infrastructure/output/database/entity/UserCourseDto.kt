package mx.edu.iuv.monitor.infrastructure.output.database.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class UserCourseDto (

    @Id
    val courseId: Long,

    val courseShortName: String = "",

    val courseUserActive: Boolean = false,

    val courseLastAccess: Long,

)
