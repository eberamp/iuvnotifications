package mx.edu.iuv.monitor.infrastructure.output.transformation.reporter

import mx.edu.iuv.monitor.infrastructure.output.database.entity.NotificationLog
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.springframework.stereotype.Component

@Component
class DefaultReportMapperStrategy: ReportMapperStrategy {

    override suspend fun buildWorkBook(notifications: List<NotificationLog>): SXSSFWorkbook {

        val workbook = SXSSFWorkbook()
        workbook.shouldCalculateSheetDimensions()
        val sheet = workbook.createSheet("report" + System.currentTimeMillis())

        val headerRow = sheet.createRow(0)
        val headers = arrayOf(
            "id", "type", "reason", "userId", "userRole", "userName",
            "courseId", "courseName",
            "dateSent", "courseLastAccess", "platformLastAccess")

        for ((index, header) in headers.withIndex()) {
            val cell = headerRow.createCell(index)
            cell.setCellValue(header)
        }

        var rowNum = 1
        for (notification in notifications) {
            val row = sheet.createRow(rowNum++)
            row.createCell(0).setCellValue(notification.id.toString())
            row.createCell(1).setCellValue(notification.type.name)
            row.createCell(2).setCellValue(notification.reason.name)
            row.createCell(3).setCellValue(notification.user.id)
            row.createCell(4).setCellValue(notification.user.userRole)
            row.createCell(5).setCellValue(notification.user.firstname)
            row.createCell(6).setCellValue(notification.user.courseId)
            row.createCell(7).setCellValue(notification.user.courseName)
            row.createCell(8).setCellValue(getZonedLocalDateTime(notification.dateSent))
            row.createCell(9).setCellValue(getZonedLocalDateTime(notification.user.lastCourseAccess))
            row.createCell(10).setCellValue(getZonedLocalDateTime(notification.user.lastPlatformAccess))
        }

        sheet.trackAllColumnsForAutoSizing()
        for (index in 1..headers.lastIndex) {
            sheet.autoSizeColumn(index)
        }

        return workbook;
    }

}