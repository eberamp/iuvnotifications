package mx.edu.iuv.monitor.infrastructure.output.messaging

import jakarta.mail.Address
import jakarta.mail.internet.MimeMessage
import mx.edu.iuv.monitor.domain.const.NotificationReason
import mx.edu.iuv.monitor.domain.const.NotificationResult
import mx.edu.iuv.monitor.domain.model.Notification
import mx.edu.iuv.monitor.domain.service.output.NotificationSender
import mx.edu.iuv.monitor.infrastructure.output.database.const.HtmlEmailTemplate
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Service
@EnableConfigurationProperties(HtmlEmailTemplate::class)
class NotificationChannelHandler (
    private val mailSender: JavaMailSender,
    private val templateEngine: TemplateEngine,
    private val htmlEmailTemplate: HtmlEmailTemplate
) : NotificationSender {

    fun testNotifyViaEmail(notifications: List<Notification>): NotificationResult {
        println("Sending Notifications...")

        try {

            val message = mailSender.createMimeMessage()
            message.setFrom("coordinacion_tecnicopedagogica@iuv.edu.mx")

            message.setRecipients(MimeMessage.RecipientType.TO, "eamp143112@gmail.com")
            message.setRecipients(MimeMessage.RecipientType.CC, "coordinacion_tecnicopedagogica@iuv.edu.mx")

            message.subject = "Test email from my Springapplication"

            val context = Context()
            context.setVariable(USER_NAME_PLACEHOLDER, notifications.first().recipient.firstName)
            context.setVariable(COURSE_NAME_PLACEHOLDER, notifications.first().recipient.courses.first()?.shortName)
            val bodyHtmlMessage = getPersonalizedEmailTemplateForContext(context, notifications.first().reason)

            message.setContent(bodyHtmlMessage, "text/html; charset=utf-8");

            mailSender.send(message)

        } catch (e: Exception){
            println(e)
            return NotificationResult.Failure("Mail Service Failed", notifications)
        }

        return NotificationResult.Success("Notification Test Successful", notifications)

    }

    override fun notifyViaEmail(notification: Notification): NotificationResult {

        // TODO: Implement email sender

        return NotificationResult.Success("Successful", notification)

    }

    override fun notifyViaEmail(notifications: List<Notification>): NotificationResult {

        val failedSendNotifications: MutableList<Notification> = mutableListOf()

        notifications.forEach { notification ->
            val message = mailSender.createMimeMessage()
            try {
                message.setFrom("coordinacion_tecnicopedagogica@iuv.edu.mx")
                message.setRecipients(MimeMessage.RecipientType.TO, notification.recipient.email)
                message.setRecipients(MimeMessage.RecipientType.CC, "coordinacion_tecnicopedagogica@iuv.edu.mx")
                message.subject = "Notificación IUV"

                val context = Context()
                context.setVariable(USER_NAME_PLACEHOLDER, notification.recipient.firstName)
                context.setVariable(COURSE_NAME_PLACEHOLDER, notification.message)

                val bodyHtmlMessage = getPersonalizedEmailTemplateForContext(context, notification.reason)
                message.setContent(bodyHtmlMessage, "text/html; charset=utf-8")

                mailSender.send(message)
            } catch (e: Exception){
                // log notification with error
                failedSendNotifications.add(notification)
            }
        }

        return if (failedSendNotifications.isEmpty()){
            NotificationResult.Success("Successful", notifications)
        } else {
            NotificationResult.Failure("Some notifications failed to be sent", failedSendNotifications)
        }
    }

    override fun notifyViaWhatsApp(notification: Notification): NotificationResult {

        // TODO: Implement whatsApp sender

        return NotificationResult.Success("Successful", notification)

    }

    override fun notifyViaWhatsApp(notifications: List<Notification>): NotificationResult {

        // TODO: Implement whatsApp sender

        return NotificationResult.Success("Successful", notifications)

    }

    private fun getPersonalizedEmailTemplateForContext(context: Context, reason: NotificationReason): String {
        // TODO: add userRole or userType to IuvUser definition to distinguish between them
        return when(reason){
            NotificationReason.COURSE_INACTIVITY_24_HOURS ->
                templateEngine.process(htmlEmailTemplate.teacher.courseInactive, context)
            NotificationReason.COURSE_ACTIVITIES_PENDING_GRADING ->
                templateEngine.process(htmlEmailTemplate.teacher.courseActivitiesPendingGrading, context)
            NotificationReason.COURSE_MISSING_WELCOME_MESSAGE ->
                templateEngine.process(htmlEmailTemplate.teacher.courseMissingWelcomeMessage, context)
            else -> templateEngine.process(htmlEmailTemplate.default, context)
        }
    }

    companion object {
        const val USER_NAME_PLACEHOLDER = "user_name"
        const val COURSE_NAME_PLACEHOLDER = "course_name"
        const val BODY_MESSAGE_PLACEHOLDER = "body_message"
    }

}
