package mx.edu.iuv.monitor.infrastructure.output.transformation.reporter

import mx.edu.iuv.monitor.domain.const.ReportType
import org.springframework.stereotype.Component

@Component
class ReportMapperFactory (
    val reportWorkBookMappers: Map<String, ReportMapperStrategy>,
    val defaultMapper: DefaultReportMapperStrategy
){

    fun getReportWorkBookMapperForType(reportType: ReportType): ReportMapperStrategy {
        val strategyName = when(reportType){
            ReportType.TEACHER_ALL -> "courseActivitiesReportMapperStrategy"
            ReportType.TEACHER_COURSE_INACTIVITY -> "courseInactivityReportMapperStrategy"
            ReportType.TEACHER_COURSE_MISSING_WELCOME_MESSAGE -> ""
            ReportType.TEACHER_COURSE_ACTIVITIES_PENDING_GRADING -> "activitiesReportMapperStrategy"
            else -> "DefaultReportMapperStrategy"
        }

        return reportWorkBookMappers[strategyName] ?: defaultMapper
    }

}