package mx.edu.iuv.monitor.domain

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class MonitoringTask(
    private val teacherNotifier: TeacherNotifier
) {

    @Scheduled(fixedRate = 5000)
    fun sendTeacherNotifications(){

        println("Test running every 3s")

        teacherNotifier.notifyAllTeachersCourseInactivity()
        // teacherNotifier.notifyAllPendingScoring()

    }


}