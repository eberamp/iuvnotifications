package mx.edu.iuv.monitor.infrastructure.output.transformation

import mx.edu.iuv.monitor.domain.const.ReportType
import mx.edu.iuv.monitor.domain.service.output.NotificationReporter
import mx.edu.iuv.monitor.domain.service.output.NotificationRepository
import mx.edu.iuv.monitor.infrastructure.output.transformation.reporter.ReportMapperFactory
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class NotificationReporterAdapter (
    private val notificationRepository: NotificationRepository,
    private val reportMapperFactory: ReportMapperFactory
): NotificationReporter {

    override suspend fun createReportLast24Hours(): ByteArray {
        val notificationsSent = notificationRepository.findAllSentLast24Hours()
        if (notificationsSent.isEmpty()) return ByteArray(0)

        val workbook = reportMapperFactory
            .getReportWorkBookMapperForType(ReportType.TEACHER_ALL)
            .buildWorkBook(notificationsSent)

        val outputStream = ByteArrayOutputStream()
        workbook.let {
            it.write(outputStream)
            it.close()
            outputStream.close()
        }

        return outputStream.toByteArray()
    }

    override suspend fun createReportLast24Hours(reportType: ReportType): ByteArray {

        val notificationsSent = notificationRepository.findAllSentLast24Hours()
        if (notificationsSent.isEmpty()) return ByteArray(0)

        val workbook = reportMapperFactory
            .getReportWorkBookMapperForType(reportType)
            .buildWorkBook(notificationsSent)

        val outputStream = ByteArrayOutputStream()
        workbook.let {
            it.write(outputStream)
            it.close()
            outputStream.close()
        }

        return outputStream.toByteArray()
    }

    override suspend fun createReportFromOneWeekAgo(reportType: ReportType): ByteArray {
        TODO("Not yet implemented")
    }

    override suspend fun createReportFromDaysAgo(reportType: ReportType, daysAgo: Long): ByteArray {

        val notificationsSent = notificationRepository.findAllSentDaysAgo(daysAgo)
        if (notificationsSent.isEmpty()) return ByteArray(0)

        val workbook = reportMapperFactory
            .getReportWorkBookMapperForType(reportType)
            .buildWorkBook(notificationsSent)

        // TODO: This handles the workbook sheet info needs to be moved and also create a Date Utils class
        val sheetInfo = workbook.createSheet("information")
        val infoHeaderRow = sheetInfo.createRow(0)
        val infoHeaders = arrayOf("Report type", "Date generated", "Timestamp", "Number of notifications")

        for ((index, header) in infoHeaders.withIndex()) {
            val cell = infoHeaderRow.createCell(index)
            cell.setCellValue(header)
        }

        val row = sheetInfo.createRow(1)
        row.createCell(0).setCellValue(reportType.name)

        val now = Date().toInstant().atZone(ZoneId.of("UTC-6"))
        row.createCell(1).setCellValue(now.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
        row.createCell(2).setCellValue(now.toEpochSecond().toString())
        row.createCell(3).setCellValue(notificationsSent.size.toString())

        sheetInfo.trackAllColumnsForAutoSizing()
        for (index in 1..infoHeaders.lastIndex) {
            sheetInfo.autoSizeColumn(index)
        }
        // TODO: End

        val outputStream = ByteArrayOutputStream()

        workbook.let {
            it.write(outputStream)
            it.close()
            outputStream.close()
        }

        return outputStream.toByteArray()
    }

}