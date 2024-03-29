
package mx.edu.iuv.monitor.domain

import mx.edu.iuv.monitor.domain.const.NotificationResult
import mx.edu.iuv.monitor.domain.const.NotificationType
import mx.edu.iuv.monitor.domain.exception.NotificationException
import mx.edu.iuv.monitor.domain.exception.NotificationSenderException
import mx.edu.iuv.monitor.domain.model.Notification
import mx.edu.iuv.monitor.domain.service.output.NotificationSender
import org.springframework.stereotype.Service

/**
 * A Notification Orchestrator
 *
 * This class orchestrates and delegates each notification type to the respective Sender adapter
 *
 * @property notificationSender the *notification sender* to send a notification via a channel (email, whatsapp, etc)
 */

@Service
class NotificationOrchestrator (
    private val notificationSender: NotificationSender
) {

    fun sendNotification(notification: Notification){

        // Possibly check user/recipient notification config

        val result: NotificationResult = when (notification.type){
            NotificationType.EMAIL -> notificationSender.notifyViaEmail(notification)
            NotificationType.WHATSAPP -> notificationSender.notifyViaWhatsApp(notification)
        }

        when (result) {
            is NotificationResult.Success -> {
                // Record notification to custom database
                println("Email sent successfully")
            }
            is NotificationResult.Failure -> {
                throw NotificationSenderException(result.errorMessage)
            }
        }

    }

    fun sendNotifications(notifications: List<Notification>){
        // TODO: make functions suspended to enable asynchronicity
        sendEmailNotifications(notifications)
    }

    private fun sendEmailNotifications(notifications: List<Notification>){
        val emailNotifications = notifications.filter { it.type == NotificationType.EMAIL }

        val result = notificationSender.notifyViaEmail(emailNotifications)
        when (result) {
            is NotificationResult.Success -> {
                println(result.message)
                // Record notification to custom database
            }
            is NotificationResult.Failure -> {
                throw NotificationSenderException(result.errorMessage)
            }
        }
    }

}
