package mx.edu.iuv.monitor.domain.service.output

import org.springframework.stereotype.Service

interface NotificationReporter {
    fun createReportLast24Hours(): ByteArray

}