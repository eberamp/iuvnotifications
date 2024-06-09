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

/*
    Each notification should be treated individually and be unique, although we could prepare the message parameter
    to include the html template that would be costly in terms of memory, instead we build the html message
    with a template engine using the course shortname
 */


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

            //println(inactiveTeachers)

            // TODO: maybe use a List<Pair<Key, Value>> for the message and refactor to messageValues
            val notifications = inactiveTeachers.map { user ->
                Notification(
                    type = NotificationType.EMAIL,
                    reason = NotificationReason.COURSE_INACTIVITY_24_HOURS,
                    recipient = user,
                    message = "",
                    from = designatedTeacherCoordinatorEmail,
                    dateSent = Date()
                )

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

    override fun notifyAllTeachersReminderCourseWelcomingMessage() {
        try {
            val teacherMissingWelcomeMessage = teacherRepository.getAllCourseMissingWelcomeMessageLast24Hours()
            // val alreadyNotified = notificationRepository.getAllNotifiedTeachersLast24Hours()
            // TODO: maybe check user notification preferences or system flags for notification type

            println(teacherMissingWelcomeMessage.firstOrNull())


            // TODO: maybe use a List<Pair<Key, Value>> for the message and refactor to messageValues
            val notifications = teacherMissingWelcomeMessage.map { user ->
                Notification(
                    type = NotificationType.EMAIL,
                    reason = NotificationReason.COURSE_MISSING_WELCOME_MESSAGE,
                    recipient = user,
                    message = "",
                    from = designatedTeacherCoordinatorEmail,
                    dateSent = Date()
                )

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

    override fun notifyAllTeachersActivitiesPendingGrading(){
        try {
            val teachersWithActivitiesPendingGrading = teacherRepository.getAllCourseActivitiesPendingGrading()
            // val alreadyNotified = notificationRepository.getAllNotifiedTeachersLast24Hours()
            // TODO: maybe check user notification preferences or system flags for notification type

            println(teachersWithActivitiesPendingGrading.firstOrNull())

            // TODO: maybe use a List<Pair<Key, Value>> for the message and refactor to messageValues
            val notifications = teachersWithActivitiesPendingGrading.map { user ->
                Notification(
                    type = NotificationType.EMAIL,
                    reason = NotificationReason.COURSE_ACTIVITIES_PENDING_GRADING,
                    recipient = user,
                    message = "",
                    from = designatedTeacherCoordinatorEmail,
                    dateSent = Date()
                )

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



}