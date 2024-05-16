package mx.edu.iuv.monitor.infrastructure.output.transformation

import mx.edu.iuv.monitor.domain.service.output.NotificationReporter
import mx.edu.iuv.monitor.domain.service.output.NotificationRepository
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class NotificationReporterAdapter (
    private val notificationRepository: NotificationRepository
): NotificationReporter {

    override fun createReportLast24Hours(): ByteArray {

        val notificationsSent = notificationRepository.getNotificationsSentLast24Hours()
        if (notificationsSent.isEmpty()) return ByteArray(0)

        val workbook = SXSSFWorkbook()
        workbook.shouldCalculateSheetDimensions()
        val sheet = workbook.createSheet("report" + System.currentTimeMillis())

        val headerRow = sheet.createRow(0)
        val headers = arrayOf("id", "type", "reason", "userId", "userName", "courseId", "courseName", "dateSent")
        for ((index, header) in headers.withIndex()) {
            val cell = headerRow.createCell(index)
            cell.setCellValue(header)
        }

        var rowNum = 1
        for (notification in notificationsSent) {
            val row = sheet.createRow(rowNum++)
            row.createCell(0).setCellValue(notification.id.toString())
            row.createCell(1).setCellValue(notification.type.name)
            row.createCell(2).setCellValue(notification.reason.name)
            row.createCell(3).setCellValue(notification.user.id)
            row.createCell(4).setCellValue(notification.user.firstname)
            row.createCell(5).setCellValue(notification.user.courseId)
            row.createCell(6).setCellValue(notification.user.courseName)
            row.createCell(7).setCellValue(getLocalFormattedDate(notification.dateSent))
        }

        sheet.trackAllColumnsForAutoSizing()
        for (index in 1..headers.lastIndex) {
            sheet.autoSizeColumn(index)
        }

        val outputStream = ByteArrayOutputStream()
        workbook.write(outputStream)
        workbook.close()
        outputStream.close()
        return outputStream.toByteArray()

    }

    private fun getLocalFormattedDate(date: Date): String {
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(format)
    }

}