
package mx.edu.iuv.monitor.domain.service

import mx.edu.iuv.monitor.domain.model.Notification
import kotlin.time.Duration

interface Scheduler {

    fun scheduleNotification(notification: Notification, atTime: Duration)

}