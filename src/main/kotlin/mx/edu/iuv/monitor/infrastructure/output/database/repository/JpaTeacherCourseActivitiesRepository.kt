package mx.edu.iuv.monitor.infrastructure.output.database.repository

import mx.edu.iuv.monitor.infrastructure.output.database.entity.TeacherWithActivitiesSummaryEntity
import mx.edu.iuv.monitor.infrastructure.output.database.query.TeacherQuery
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface JpaTeacherCourseActivitiesRepository: JpaRepository<TeacherWithActivitiesSummaryEntity, Long> {

    @Query(nativeQuery = true, value = TeacherQuery.GET_ALL_TEACHERS_WITH_COURSE_ACTIVITIES_PENDING_GRADING)
    fun runQueryGetAllTeachersCourseActivitiesPendingGrading(): List<TeacherWithActivitiesSummaryEntity>

}