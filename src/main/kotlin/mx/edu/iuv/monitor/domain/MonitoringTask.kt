package mx.edu.iuv.monitor.domain

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class MonitoringTask(
    private val teacherNotifier: TeacherNotifier
) {

    @Scheduled(fixedRate = 60 * 1000 * 10)
    fun sendTeacherNotifications(){

        println("Notifying teachers...")

        //teacherNotifier.notifyAllTeachersCourseInactivity()
        //teacherNotifier.notifyAllReminderCourseWelcomingMessage()
        //teacherNotifier.notifyAllPendingScoring()

    }


}