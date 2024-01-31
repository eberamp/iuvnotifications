package mx.edu.iuv.monitor.domain.service.output

import mx.edu.iuv.monitor.domain.const.NotificationResult
import mx.edu.iuv.monitor.domain.model.Notification

interface NotificationSender {

    fun notifyViaEmail(notification: Notification): NotificationResult
    fun notifyViaEmail(notification: List<Notification>): NotificationResult
    fun notifyViaWhatsApp(notification: Notification): NotificationResult
    fun notifyViaWhatsApp(notification: List<Notification>): NotificationResult

}