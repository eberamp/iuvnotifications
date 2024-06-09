package mx.edu.iuv.monitor.infrastructure.output.database.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.MappedSuperclass
import java.io.Serializable

data class TeacherCourseCompositeId (
    val userId: Long = 0L,
    val courseId: Long = 0L
) : Serializable

@MappedSuperclass
@IdClass(TeacherCourseCompositeId::class)
abstract class TeacherEntity {

    @Id
    @Column(name = "user_id", nullable = false)
    val userId: Long = 0L

    @Id
    @Column(name = "course_id", nullable = false)
    val courseId: Long = 0L

}