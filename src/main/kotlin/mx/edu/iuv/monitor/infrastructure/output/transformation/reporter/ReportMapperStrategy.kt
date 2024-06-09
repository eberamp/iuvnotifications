package mx.edu.iuv.monitor.infrastructure.output.transformation.reporter

import mx.edu.iuv.monitor.infrastructure.output.database.entity.NotificationLog
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.springframework.cglib.core.Local
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date

interface ReportMapperStrategy {

    suspend fun buildWorkBook(notifications: List<NotificationLog>): SXSSFWorkbook

    fun getZonedLocalDateTime(date: Date): LocalDateTime {
        return date.toInstant().atZone(ZONE_AMERICA_MEXICO_CITY).toLocalDateTime()
    }

    fun getFormattedDate(dateTime: LocalDateTime): String {
        return dateTime.format(DATE_FORMAT_YEAR_MONTH_DAY_TIME)
    }

    fun getDifferenceInDaysFromNow(date: Date): Long {
        val now: LocalDateTime = ZonedDateTime.now().withZoneSameInstant(ZONE_UTC).toLocalDateTime()
        val other: LocalDateTime = LocalDateTime.ofInstant(date.toInstant(), ZONE_UTC)
        return ChronoUnit.DAYS.between(now, other)
    }

    fun getDifferenceInDays(dateFrom: Date, dateTo: Date): Long {
        val from: LocalDateTime = LocalDateTime.ofInstant(dateFrom.toInstant(), ZONE_UTC)
        val other: LocalDateTime = LocalDateTime.ofInstant(dateTo.toInstant(), ZONE_UTC)
        return ChronoUnit.DAYS.between(from, other)
    }

    fun getCurrentZonedLocalDateTime() = ZonedDateTime.now().withZoneSameInstant(ZONE_AMERICA_MEXICO_CITY).toLocalDateTime()
    
    companion object {
        val ZONE_AMERICA_MEXICO_CITY: ZoneId = ZoneId.of("UTC-6")
        val ZONE_UTC: ZoneId = ZoneId.of("UTC")
        val DATE_FORMAT_YEAR_MONTH_DAY_TIME: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    }

}