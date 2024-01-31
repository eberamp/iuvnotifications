
package mx.edu.iuv.monitor.domain.service.input

import mx.edu.iuv.monitor.domain.exception.NotificationException

interface NotifyTeacher {

    @Throws(NotificationException::class)
    fun notifyAllTeachersCourseInactivity()

    fun notifyAllPendingScoring()
    fun notifyAllReminderCourseWelcomingMessage()

}