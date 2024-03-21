package mx.edu.iuv.monitor.infrastructure.output.database.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import java.io.Serializable

class UserCourseCompositeId : Serializable {
    val userId: Long = 0L
    val courseId: Long = 0L
}

@Entity
@IdClass(UserCourseCompositeId::class)
data class IuvUserWithCourseSummaryEntity (

    @Id
    val userId: Long = 0L,
    val firstName: String = "",
    val email: String = "",
    val lastAccess: Long = 0L,

    @Id
    val courseId: Long = 0L,
    val courseShortName: String = "",
    val courseUserActive: Int = 0,
    val courseLastAccess: Long = 0L,

)