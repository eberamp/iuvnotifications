package mx.edu.iuv.monitor.infrastructure.input.web.controller

import mx.edu.iuv.monitor.domain.service.output.NotificationReporter
import mx.edu.iuv.monitor.domain.service.output.NotificationRepository
import mx.edu.iuv.monitor.infrastructure.output.database.entity.NotificationLog
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/monitor/api")
@Validated
class MonitorController (
    private val notificationReporter: NotificationReporter,
    private val notificationRepository: NotificationRepository
) {

    @GetMapping("/report/csv")
    fun generateCSVReport(): ResponseEntity<ByteArray> {
        val byteArray = notificationReporter.createReportLast24Hours()
        if(byteArray.isEmpty()){
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }

        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${REPORT_FILENAME_CSV}")

        return ResponseEntity(byteArray, headers, HttpStatus.OK)
    }

    @GetMapping("/report/xls")
    fun generateXLSReport(): ResponseEntity<ByteArray> {
        val byteArray = notificationReporter.createReportLast24Hours()
        if(byteArray.isEmpty()){
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }

        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${REPORT_FILENAME_XLS}")

        return ResponseEntity(byteArray, headers, HttpStatus.OK)
    }

    @GetMapping("/notifications/{id}")
    fun getNotificationById(@PathVariable("id") notificationId: String): ResponseEntity<NotificationLog> {

        val notification = notificationRepository.findById(notificationId)
        return if (notification.isPresent){
            ResponseEntity(notification.get(), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }

    }

    @GetMapping("/users/{id}/notifications")
    fun getNotificationByUserId(@PathVariable("id") userId: String): ResponseEntity<List<NotificationLog>> {

        val userNotifications = notificationRepository.findByUserId(userId)
        return if (userNotifications.isNotEmpty()){
            ResponseEntity(userNotifications, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }

    }

    @GetMapping("/notifications/search")
    fun getNotificationsSentOn(
        @RequestParam("dateSent") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) dateSent: LocalDate
    ): ResponseEntity<List<NotificationLog>> {

        val userNotifications = notificationRepository.getNotificationsSentOn(dateSent)
        return if (userNotifications.isNotEmpty()){
            ResponseEntity(userNotifications, HttpStatus.OK)
        } else {
            ResponseEntity(null, HttpStatus.NOT_FOUND)
        }

    }

    companion object {
        private val REPORT_FILENAME_CSV = "report_" + System.currentTimeMillis() + ".csv"
        private val REPORT_FILENAME_XLS = "report_" + System.currentTimeMillis() + ".xls"
    }

}