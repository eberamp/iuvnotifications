package mx.edu.iuv.monitor.domain.service.output

import mx.edu.iuv.monitor.domain.const.ReportType

interface NotificationReporter {

    suspend fun createReportLast24Hours(): ByteArray
    suspend fun createReportLast24Hours(reportType: ReportType): ByteArray
    suspend fun createReportFromOneWeekAgo(reportType: ReportType): ByteArray
    suspend fun createReportFromDaysAgo(reportType: ReportType, daysAgo: Long): ByteArray

}