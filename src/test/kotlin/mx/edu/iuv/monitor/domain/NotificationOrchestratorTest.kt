package mx.edu.iuv.monitor.domain

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import mx.edu.iuv.monitor.domain.const.NotificationReason
import mx.edu.iuv.monitor.domain.const.NotificationResult
import mx.edu.iuv.monitor.domain.const.NotificationType
import mx.edu.iuv.monitor.domain.exception.NotificationSenderException
import mx.edu.iuv.monitor.domain.model.IuvUser
import mx.edu.iuv.monitor.domain.model.Notification
import mx.edu.iuv.monitor.domain.service.output.NotificationRepository
import mx.edu.iuv.monitor.domain.service.output.NotificationSender
import kotlin.test.assertFailsWith
import java.util.Date
import kotlin.test.Test
import kotlin.test.BeforeTest

class NotificationOrchestratorTest {

    private lateinit var notificationOrchestrator: NotificationOrchestrator
    private val notificationSender = mockk<NotificationSender>()
    private val notificationRepository = mockk<NotificationRepository>()

    @BeforeTest
    fun `set Up`(){
        notificationOrchestrator = NotificationOrchestrator(notificationSender, notificationRepository)
        every { notificationRepository.saveNotification(any(Notification::class)) } returns Unit
        every { notificationRepository.saveAllNotifications(any<List<Notification>>()) } returns Unit
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

    @Test
    fun `should filter all notification of type EMAIL when notification type is EMAIL`() {
        val notification1: Notification = createNotificationOfType(NotificationType.EMAIL)
        val notification2: Notification = createNotificationOfType(NotificationType.WHATSAPP)
        val notifications = listOf(notification1, notification2)

        val notificationResult = NotificationResult.Success("success", notification1)
        every { notificationSender.notifyViaEmail(any<List<Notification>>()) } returns notificationResult

        notificationOrchestrator.sendNotifications(notifications)
        verify(exactly = 1) { notificationSender.notifyViaEmail(listOf(notification1)) }
    }

    @Test
    fun `should record notifications when notification is sent successfully`() {

        // TODO: update test to reflect actual database record

        val notification: Notification = createNotificationOfType(NotificationType.EMAIL)
        val notificationResult = NotificationResult.Success("success", notification)

        every { notificationSender.notifyViaEmail(any(Notification::class)) } returns notificationResult

        notificationOrchestrator.sendNotification(notification)
        verify(exactly = 1) { notificationSender.notifyViaEmail(notification) }

    }

    @Test
    fun `should throw NotificationSenderException when notification is not sent successfully`() {
        val notification: Notification = createNotificationOfType(NotificationType.EMAIL)
        val notificationResult = NotificationResult.Failure("failure", notification)
        every { notificationSender.notifyViaEmail(any(Notification::class)) } returns notificationResult

        assertFailsWith(NotificationSenderException::class) {
            notificationOrchestrator.sendNotification(notification)
        }
    }

    @Test
    fun `should log failed notifications when NotificationSenderException is thrown`() {
        val notification: Notification = createNotificationOfType(NotificationType.EMAIL)
        val error = NotificationSenderException("Test error message")
        every { notificationSender.notifyViaEmail(any(Notification::class)) } throws error

        assertFailsWith(NotificationSenderException::class, "Test error message") {
            notificationOrchestrator.sendNotification(notification)
        }
    }

    private fun createNotificationOfType(notificationType: NotificationType): Notification {
        val recipient = IuvUser(
            id = 123,
            firstName = "test name",
            userRole = "teacher",
            email = "test@mail.com",
            phoneNumber = "541112222",
            lastPlatformAccess = Date(),
            courses = emptyList(),
            activities = emptyList()
        )

        return Notification(
            recipient = recipient,
            from = "admin@mail.com",
            type = notificationType,
            message = "Email test message",
            dateSent = Date(),
            reason = NotificationReason.COURSE_INACTIVITY_24_HOURS
        )
    }
}