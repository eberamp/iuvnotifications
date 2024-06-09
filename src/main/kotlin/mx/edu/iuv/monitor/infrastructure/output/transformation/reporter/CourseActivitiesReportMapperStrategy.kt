package mx.edu.iuv.monitor.infrastructure.output.transformation.reporter

import mx.edu.iuv.monitor.infrastructure.output.database.entity.NotificationLog
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.springframework.stereotype.Component

@Component
class CourseActivitiesReportMapperStrategy: ReportMapperStrategy {

    override suspend fun buildWorkBook(notifications: List<NotificationLog>): SXSSFWorkbook {

        val workbook = SXSSFWorkbook()
        workbook.shouldCalculateSheetDimensions()
        val sheet = workbook.createSheet("notifications")

        val headerRow = sheet.createRow(0)
        val headers = arrayOf(
            "notificationId", "type", "reason", "userId", "userRole", "userName",
            "courseId", "courseName", "activityItemId", "activityItemName", "studentId", "studentName",
            "dateSent", "courseLastAccess", "courseLastAccessDays", "platformLastAccess", "platformLastAccessDays")

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
            row.createCell(8).setCellValue(notification.user.activityItemId)
            row.createCell(9).setCellValue(notification.user.activityItemName)
            row.createCell(10).setCellValue("")
            row.createCell(11).setCellValue("")

            val dateSent = notification.dateSent;

            row.createCell(12).setCellValue(
                getFormattedDate(getZonedLocalDateTime(dateSent))
            )
            row.createCell(13).setCellValue(
                getFormattedDate(getZonedLocalDateTime(notification.user.lastCourseAccess))
            )
            row.createCell(14).setCellValue(
                getDifferenceInDays(notification.user.lastCourseAccess, dateSent).toString()
            )
            row.createCell(15).setCellValue(
                getFormattedDate(getZonedLocalDateTime(notification.user.lastPlatformAccess))
            )
            row.createCell(16).setCellValue(
                getDifferenceInDays(notification.user.lastPlatformAccess, dateSent).toString()
            )
        }

        sheet.trackAllColumnsForAutoSizing()
        for (index in 1..headers.lastIndex) {
            sheet.autoSizeColumn(index)
        }

        return workbook
    }

}