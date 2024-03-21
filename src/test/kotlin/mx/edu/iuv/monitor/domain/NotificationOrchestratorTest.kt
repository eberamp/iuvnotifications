package mx.edu.iuv.monitor.domain

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import mx.edu.iuv.monitor.domain.const.NotificationResult
import mx.edu.iuv.monitor.domain.const.NotificationType
import mx.edu.iuv.monitor.domain.model.IuvUser
import mx.edu.iuv.monitor.domain.model.Notification
import mx.edu.iuv.monitor.domain.service.output.NotificationSender
import java.util.Date
import kotlin.test.Test

import kotlin.test.BeforeTest

class NotificationOrchestratorTest {

    private lateinit var notificationOrchestrator: NotificationOrchestrator
    private val notificationSender = mockk<NotificationSender>()

    @BeforeTest
    fun `set Up`(){
        notificationOrchestrator = NotificationOrchestrator(notificationSender)
    }

    @Test
    fun `should delegate notification via email when notification type is EMAIL`() {

        val notification: Notification = createNotificationOfType(NotificationType.EMAIL)
        val notificationResult = NotificationResult.Success("success", notification)

        every { notificationSender.notifyViaEmail(any(Notification::class)) } returns notificationResult

        notificationOrchestrator.sendNotification(notification)
        verify(exactly = 1) { notificationSender.notifyViaEmail(notification) }

    }

    @Test
    fun `should delegate notification via whatsApp when notification type is WhatsApp`() {

        val notification: Notification = createNotificationOfType(NotificationType.WHATSAPP)
        val notificationResult = NotificationResult.Success("success", notification)

        every { notificationSender.notifyViaWhatsApp(any(Notification::class)) } returns notificationResult

        notificationOrchestrator.sendNotification(notification)
        verify(exactly = 1) { notificationSender.notifyViaWhatsApp(notification) }

    }

    private fun createNotificationOfType(notificationType: NotificationType): Notification {
        val recipient = IuvUser(
            id = 123,
            firstName = "test name",
            email = "test@mail.com",
            phoneNumber = "541112222",
            lastAccess = Date(),
            courses = listOf()
        )

        return Notification(
            recipient = recipient,
            from = "admin@mail.com",
            type = notificationType,
            message = "Email test message",
            dateSent = Date()
        )
    }
}