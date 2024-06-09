package mx.edu.iuv.monitor.domain.service.output

import mx.edu.iuv.monitor.domain.model.IuvUser

interface TeacherRepository {

    fun getAllCourseInactiveLast24Hours(): List<IuvUser>
    fun getAllCourseMissingWelcomeMessageLast24Hours(): List<IuvUser>
    fun getAllCourseActivitiesPendingGrading(): List<IuvUser>

}