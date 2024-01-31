package mx.edu.iuv.monitor.infrastructure.output.messaging

import mx.edu.iuv.monitor.domain.const.NotificationResult
import mx.edu.iuv.monitor.domain.model.Notification
import mx.edu.iuv.monitor.domain.service.output.NotificationSender
import org.springframework.stereotype.Service

@Service
class NotificationChannelHandler: NotificationSender {

    override fun notifyViaEmail(notification: Notification): NotificationResult {

        // TODO: Implement email sender

        return NotificationResult.Success("Successful", notification)

    }

    override fun notifyViaEmail(notifications: List<Notification>): NotificationResult {

        // TODO: Implement email sender

        return NotificationResult.Success("Successful", notifications)

    }

    override fun notifyViaWhatsApp(notification: Notification): NotificationResult {

        // TODO: Implement whatsApp sender

        return NotificationResult.Success("Successful", notification)

    }

    override fun notifyViaWhatsApp(notification: List<Notification>): NotificationResult {

        // TODO: Implement whatsApp sender

        return NotificationResult.Success("Successful", notification)

    }

}
