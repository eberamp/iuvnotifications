package mx.edu.iuv.monitor.domain

import mx.edu.iuv.monitor.domain.const.NotificationReason
import mx.edu.iuv.monitor.domain.const.NotificationType
import mx.edu.iuv.monitor.domain.exception.NotificationException
import mx.edu.iuv.monitor.domain.exception.NotificationSenderException
import mx.edu.iuv.monitor.domain.exception.RepositoryException
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
            val inactiveTeachers = teacherRepository.getAllCourseInactiveLast24Hours()
            // val alreadyNotified = notificationRepository.getAllNotifiedTeachersLast24Hours()
            // TODO: maybe check user notification preferences or system flags for notification type

            /*
            Each notification should be treated individually and be unique, although we could prepare the message parameter
            to include the html template that would be costly in terms of memory, instead we only send what differs for
            each notification for a single user, as a user could have many courses where they have been inactive,
            and later we build the html message with a template engine using the course shortname
             */
            // TODO: maybe use a List<Pair<Key, Value>> for the message and refactor to messageValues
            val notifications = inactiveTeachers.flatMap { user ->
                user.courses.filterNotNull().map {
                    Notification(
                        type = NotificationType.EMAIL,
                        reason = NotificationReason.COURSE_INACTIVITY_24_HOURS,
                        recipient = user,
                        message = it.shortName,
                        from = designatedTeacherCoordinatorEmail,
                        dateSent = Date()
                    )
                }
            }

            notificationOrchestrator.sendNotifications(notifications)

        } catch (e: RepositoryException) {
            // log exception
            throw NotificationException("Error Code", e)
        } catch (e: NotificationSenderException) {
            // log exception
            throw NotificationException("Error Code", e)
        }
    }

    override fun notifyAllReminderCourseWelcomingMessage() {

    }

    override fun notifyAllPendingScoring(){

    }



}