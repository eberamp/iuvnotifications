
package mx.edu.iuv.monitor.domain.model

import mx.edu.iuv.monitor.domain.const.NotificationReason
import java.util.Date
import mx.edu.iuv.monitor.domain.const.NotificationType

data class Notification (
    val id: Long? = null,
    val type: NotificationType,
    val reason: NotificationReason,
    val recipient: IuvUser,
    val from: String,
    val message: String = "",
    val dateSent: Date? = null
)