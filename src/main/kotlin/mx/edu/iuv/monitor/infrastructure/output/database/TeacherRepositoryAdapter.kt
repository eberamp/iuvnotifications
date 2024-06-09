package mx.edu.iuv.monitor.infrastructure.output.database

import mx.edu.iuv.monitor.domain.exception.RepositoryException
import mx.edu.iuv.monitor.domain.model.CourseActivity
import mx.edu.iuv.monitor.domain.model.IuvUser
import mx.edu.iuv.monitor.domain.model.UserCourse
import mx.edu.iuv.monitor.domain.service.output.TeacherRepository
import mx.edu.iuv.monitor.infrastructure.output.database.const.MoodleUserStatus
import mx.edu.iuv.monitor.infrastructure.output.database.repository.JpaTeacherCourseActivitiesRepository
import mx.edu.iuv.monitor.infrastructure.output.database.repository.JpaTeacherCourseRepository
import org.springframework.stereotype.Repository
import java.sql.SQLException

@Repository
class TeacherRepositoryAdapter (
    private val jpaTeacherCourseRepository: JpaTeacherCourseRepository,
    private val JpaTeacherCourseActivitiesRepository: JpaTeacherCourseActivitiesRepository
) : TeacherRepository {

    override fun getAllCourseInactiveLast24Hours(): List<IuvUser> {
        try {
            val query = jpaTeacherCourseRepository.runQueryGetAllTeachersCourseInactiveLast24Hours()

            return query.map {
                IuvUser(
                    id = it.userId,
                    firstName = it.firstName,
                    userRole = it.userRole,
                    email = it.email,
                    lastPlatformAccess = it.getLastPlatformAccessDate(),
                    courses = listOf(
                        UserCourse(
                            id = it.courseId,
                            shortName = it.courseShortName,
                            isEnrolmentStatusActive = (it.enrolmentStatus == MoodleUserStatus.ACTIVE.ordinal),
                            lastAccessedOn = it.getLastCourseAccessDate()
                        )
                    ),
                    activities = emptyList()
                )
            }

        } catch (e: SQLException) {
            // log exception
            throw RepositoryException("Ocurrio un error al buscar maestros con inactividad en curso")
        }
    }

    override fun getAllCourseMissingWelcomeMessageLast24Hours(): List<IuvUser> {
        try {
            val query = jpaTeacherCourseRepository.runQueryGetAllTeachersCourseMissingWelcomeMessageLast24Hours()

            return query.map {
                IuvUser(
                    id = it.userId,
                    firstName = it.firstName,
                    userRole = it.userRole,
                    email = it.email,
                    lastPlatformAccess = it.getLastPlatformAccessDate(),
                    courses = listOf(
                        UserCourse(
                            id = it.courseId,
                            shortName = it.courseShortName,
                            isEnrolmentStatusActive = (it.enrolmentStatus == MoodleUserStatus.ACTIVE.ordinal),
                            lastAccessedOn = it.getLastCourseAccessDate()
                        )
                    ),
                    activities = emptyList()
                )
            }

        } catch (e: SQLException){
            // log exception
            throw RepositoryException("Ocurrio un error al buscar maestros con mensaje de bienvenida aun no publicado")
        }
    }

    override fun getAllCourseActivitiesPendingGrading(): List<IuvUser> {
        try {
            val query = JpaTeacherCourseActivitiesRepository.runQueryGetAllTeachersCourseActivitiesPendingGrading()

            return query.map {
                IuvUser(
                    id = it.userId,
                    firstName = it.firstName,
                    userRole = it.userRole,
                    email = it.email,
                    lastPlatformAccess = it.getLastPlatformAccessDate(),
                    courses = listOf(
                        UserCourse(
                            id = it.courseId,
                            shortName = it.courseShortName,
                            isEnrolmentStatusActive = (it.enrolmentStatus == MoodleUserStatus.ACTIVE.ordinal),
                            lastAccessedOn = it.getLastCourseAccessDate(),
                        )
                    ),
                    activities = listOf(
                        CourseActivity(
                            itemId = it.gradeItemId,
                            itemName = it.gradeItemName,
                            courseId = it.courseId,
                            studentId = it.gradeItemStudentId,
                            studentName = it.gradeItemStudentName
                        )
                    )
                )
            }

        } catch (e: SQLException){
            // log exception
            throw RepositoryException("Ocurrio un error al buscar maestros con mensaje de bienvenida aun no publicado")
        }
    }

}