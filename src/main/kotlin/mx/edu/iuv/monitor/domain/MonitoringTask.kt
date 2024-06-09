package mx.edu.iuv.monitor.domain

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class MonitoringTask(
    private val teacherNotifier: TeacherNotifier
) {

    @Scheduled(cron = "0 0 9,21 * * *", zone = "CST")
    //@Scheduled(cron = "0 0/1 * * * *", zone = "CST")
    fun sendTeacherNotifications(){

        println("Notifying teachers...")
        teacherNotifier.notifyAllTeachersCourseInactivity()
        teacherNotifier.notifyAllTeachersReminderCourseWelcomingMessage()
        teacherNotifier.notifyAllTeachersActivitiesPendingGrading()

    }

}