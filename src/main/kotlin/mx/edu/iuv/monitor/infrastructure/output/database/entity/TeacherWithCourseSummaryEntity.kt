package mx.edu.iuv.monitor.infrastructure.output.database.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import java.io.Serializable
import java.util.*

data class UserCourseCompositeId (
    val userId: Long = 0L,
    val courseId: Long = 0L
) : Serializable

@Entity
@IdClass(UserCourseCompositeId::class)
class TeacherWithCourseSummaryEntity {

    @Id
    @Column(name = "user_id", nullable = false)
    val userId: Long = 0L

    @Column(name = "first_name", nullable = false)
    val firstName: String = ""

    val email: String = ""

    @Column(name = "last_platform_access", nullable = false)
    val lastPlatformAccess: Long = 0L

    @Id
    @Column(name = "course_id", nullable = false)
    val courseId: Long = 0L

    @Column(name = "course_shortname", nullable = false)
    val courseShortName: String = ""

    @Column(name = "user_role", nullable = false)
    val userRole: String = ""

    @Column(name = "user_enrolment_status", nullable = false)
    val enrolmentStatus: Int = 0

    @Column(name = "last_course_access", nullable = false)
    val lastCourseAccess: Long = 0L

    fun getLastPlatformAccessDate() = Date(this.lastPlatformAccess * 1000)
    fun getLastCourseAccessDate() = Date(this.lastCourseAccess * 1000)

}