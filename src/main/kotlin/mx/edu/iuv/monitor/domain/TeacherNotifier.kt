package mx.edu.iuv.monitor.domain

import mx.edu.iuv.monitor.domain.const.NotificationType
import mx.edu.iuv.monitor.domain.exception.NotificationException
import mx.edu.iuv.monitor.domain.model.Notification
import mx.edu.iuv.monitor.domain.service.input.NotifyTeacher
import mx.edu.iuv.monitor.domain.service.output.TeacherRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Date

@Service
class TeacherNotifier (
    private val notificationOrchestrator: NotificationOrchestrator,
    private val teacherRepository: TeacherRepository,
    @Value("") private val designatedTeacherCoordinatorEmail: String
): NotifyTeacher {

    override fun notifyAllTeachersCourseInactivity(){

        try {

            val inactiveTeachers = teacherRepository.getAllInactiveLast24Hours()
            // val alreadyNotified = notificationRepository.getAllNotifiedTeachersLast24Hours()

            val notifications = inactiveTeachers.map {
                Notification(
                    type = NotificationType.EMAIL,
                    recipient = it,
                    from = designatedTeacherCoordinatorEmail,
                    message = "Inactive",
                    dateSent = Date()
                )
            }

            notificationOrchestrator.sendNotifications(notifications)

        } catch (e: RuntimeException) {
            println(e)
        } catch (e: NotificationException) {
            println(e)
        }

    }

    override fun notifyAllReminderCourseWelcomingMessage() {

    }

    override fun notifyAllPendingScoring(){

    }



}