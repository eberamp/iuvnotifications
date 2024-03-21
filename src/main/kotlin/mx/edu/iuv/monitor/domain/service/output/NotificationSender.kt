package mx.edu.iuv.monitor.domain.service.output

import mx.edu.iuv.monitor.domain.const.NotificationResult
import mx.edu.iuv.monitor.domain.model.Notification

interface NotificationSender {

    fun notifyViaEmail(notification: Notification): NotificationResult
    fun notifyViaEmail(notifications: List<Notification>): NotificationResult
    fun notifyViaWhatsApp(notification: Notification): NotificationResult
    fun notifyViaWhatsApp(notifications: List<Notification>): NotificationResult

}