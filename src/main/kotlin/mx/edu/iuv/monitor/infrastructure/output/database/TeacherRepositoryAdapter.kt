package mx.edu.iuv.monitor.infrastructure.output.database

import mx.edu.iuv.monitor.domain.exception.ErrorMessage
import mx.edu.iuv.monitor.domain.exception.RepositoryException
import mx.edu.iuv.monitor.domain.model.IuvUser
import mx.edu.iuv.monitor.domain.model.UserCourse
import mx.edu.iuv.monitor.domain.service.output.TeacherRepository
import mx.edu.iuv.monitor.infrastructure.output.database.const.MOODLE_USER_STATUS
import org.springframework.stereotype.Repository
import java.sql.SQLException
import java.time.Instant
import java.util.*

@Repository
class TeacherRepositoryAdapter(
    private val jpaTeacherRepository: JpaTeacherRepository
) : TeacherRepository {

    override fun getAllCourseInactiveLast24Hours(): List<IuvUser> {
        try {
            val query = jpaTeacherRepository.runQueryGetAllTeachersCourseInactiveLast24Hours()
            return query.groupBy { it.userId }
                .map { group ->
                    val user = group.value.first()
                    IuvUser(
                        id = user.userId,
                        firstName = user.firstName,
                        email = user.email,
                        lastAccess = Date(user.lastAccess),
                        courses = group.value.map {
                            UserCourse(
                                id = it.courseId,
                                shortName = it.courseShortName,
                                isUserActive = (it.courseUserActive == MOODLE_USER_STATUS.ACTIVE.ordinal),
                                lastActivityOn = Date(it.courseLastAccess)
                            )
                        }
                    )
                }

        } catch (e: SQLException) {
            // log exception
            throw RepositoryException("Ocurrio un error al buscar maestros con inactividad en curso")
        }
    }

    override fun getAllCourseMissingWelcomeMessageLast24Hours(): List<IuvUser> {
        try {
            val query = jpaTeacherRepository.runQueryGetAllTeachersCourseMissingWelcomeMessageLast24Hours()
            return query.groupBy { it.userId }
                .map { group ->
                    val user = group.value.first()
                    IuvUser(
                        id = user.userId,
                        firstName = user.firstName,
                        email = user.email,
                        lastAccess = Date(user.lastAccess),
                        courses = group.value.map {
                            UserCourse(
                                id = it.courseId,
                                shortName = it.courseShortName,
                                isUserActive = (it.courseUserActive == MOODLE_USER_STATUS.ACTIVE.ordinal),
                                lastActivityOn = Date(it.courseLastAccess)
                            )
                        }
                    )
                }

        } catch (e: SQLException){
            // log exception
            throw RepositoryException("Ocurrio un error al buscar maestros con mensaje de bienvenida aun no publicado")
        }
    }

    override fun getAllCoursePendingScoring(): List<IuvUser> {
        TODO("Not yet implemented")
    }


}