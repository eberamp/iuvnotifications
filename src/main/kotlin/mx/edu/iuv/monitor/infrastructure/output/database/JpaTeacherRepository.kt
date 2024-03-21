package mx.edu.iuv.monitor.infrastructure.output.database

import mx.edu.iuv.monitor.infrastructure.output.database.entity.IuvUserWithCourseSummaryEntity
import mx.edu.iuv.monitor.infrastructure.output.database.query.TeacherQuery
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface JpaTeacherRepository : JpaRepository<IuvUserWithCourseSummaryEntity, Long> {

    @Query(nativeQuery = true, value = TeacherQuery.GET_ALL_TEACHERS_COURSE_INACTIVE_24_HOURS)
    fun runQueryGetAllTeachersCourseInactiveLast24Hours(): List<IuvUserWithCourseSummaryEntity>

    @Query(nativeQuery = true, value = TeacherQuery.GET_ALL_TEACHERS_COURSE_MISSING_WELCOME_MESSAGE)
    fun runQueryGetAllTeachersCourseMissingWelcomeMessageLast24Hours(): List<IuvUserWithCourseSummaryEntity>

    @Query(nativeQuery = true, value = TeacherQuery.GET_ALL_TEACHERS_WITH_ASSIGMENTS_PENDING_GRADING)
    fun runQueryGetAllTeachersWithMissingAssignmentsPendingGrading(): List<IuvUserWithCourseSummaryEntity>

}