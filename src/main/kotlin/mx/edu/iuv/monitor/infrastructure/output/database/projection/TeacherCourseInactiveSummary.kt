package mx.edu.iuv.monitor.infrastructure.output.database.projection

interface TeacherCourseInactiveSummary {

    val userId: Long
    val firstName: String
    val email: String
    val lastAccess: Long

    val courseId: Long
    val courseShortName: String
    val courseUserActive: Boolean
    val courseLastAccess: Long

}