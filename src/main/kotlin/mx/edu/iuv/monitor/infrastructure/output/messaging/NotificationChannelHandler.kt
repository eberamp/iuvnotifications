package mx.edu.iuv.monitor.infrastructure.output.messaging

import jakarta.mail.internet.MimeMessage
import mx.edu.iuv.monitor.domain.const.NotificationReason
import mx.edu.iuv.monitor.domain.const.NotificationResult
import mx.edu.iuv.monitor.domain.model.Notification
import mx.edu.iuv.monitor.domain.service.output.NotificationSender
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Service
class NotificationChannelHandler (
    private val mailSender: JavaMailSender,
    private val templateEngine: TemplateEngine
) : NotificationSender {

    fun testNotifyViaEmail(notifications: List<Notification>): NotificationResult {
        println("Sending Notifications...")

        try {

            val message = mailSender.createMimeMessage()
            message.setFrom("emailtest@eberamp.com")
            message.setRecipients(MimeMessage.RecipientType.TO, "eamp143112@gmail.com")
            message.subject = "Test email from my Springapplication";

            val context = Context()
            context.setVariable(USER_NAME_PLACEHOLDER, notifications.first().recipient.firstName)
            context.setVariable(COURSE_NAME_PLACEHOLDER, notifications.first().message)
            val bodyHtmlMessage = getPersonalizedEmailTemplateForContext(context, notifications.first().reason)

            message.setContent(bodyHtmlMessage, "text/html; charset=utf-8");

            mailSender.send(message)

        } catch (e: Exception){
            println(e)
            return NotificationResult.Failure("Failed", notifications)

        }

        return NotificationResult.Success("Notification Test Successful", notifications)

    }

    override fun notifyViaEmail(notification: Notification): NotificationResult {

        // TODO: Implement email sender

        return NotificationResult.Success("Successful", notification)

    }

    override fun notifyViaEmail(notifications: List<Notification>): NotificationResult {
        testNotifyViaEmail(notifications)

        val failedSendNotifications: MutableList<Notification> = mutableListOf()
        val message = mailSender.createMimeMessage()
        message.setFrom("emailtest@eberamp.com")

        notifications.forEach { notification ->
            message.setRecipients(MimeMessage.RecipientType.TO, notification.recipient.email)

            val context = Context()
            context.setVariable(USER_NAME_PLACEHOLDER, notification.recipient.firstName)
            context.setVariable(COURSE_NAME_PLACEHOLDER, notification.message)

            val bodyHtmlMessage = getPersonalizedEmailTemplateForContext(context, notification.reason)
            message.setContent(bodyHtmlMessage, "text/html; charset=utf-8")

            try {
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
        return when(reason){
            NotificationReason.COURSE_INACTIVITY_24_HOURS -> templateEngine.process(HTML_TEMPLATE_COURSE_INACTIVE, context)
            NotificationReason.PENDING_SCORING -> templateEngine.process(HTML_TEMPLATE_COURSE_PENDING_SCORING, context)
            NotificationReason.MISSING_COURSE_WELCOME_MESSAGE -> templateEngine.process(
                HTML_TEMPLATE_COURSE_MISSING_WELCOME_MESSAGE, context)
            else -> templateEngine.process(HTML_TEMPLATE_DEFAULT, context)
        }
    }

    companion object {
        const val HTML_TEMPLATE_COURSE_INACTIVE = "teacher_course_inactivity.html"
        const val HTML_TEMPLATE_COURSE_MISSING_WELCOME_MESSAGE = "teacher_course_inactivity.html"
        const val HTML_TEMPLATE_COURSE_PENDING_SCORING = "teacher_course_inactivity.html"
        const val HTML_TEMPLATE_DEFAULT = ""
        const val USER_NAME_PLACEHOLDER = "user_name"
        const val COURSE_NAME_PLACEHOLDER = "course_name"
        const val BODY_MESSAGE_PLACEHOLDER = "body_message"
    }

}
