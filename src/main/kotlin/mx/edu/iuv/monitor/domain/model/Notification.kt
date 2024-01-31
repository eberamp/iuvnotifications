
package mx.edu.iuv.monitor.domain.model

import java.util.Date
import mx.edu.iuv.monitor.domain.const.NotificationType

data class Notification (
    val id: Long? = null,
    val type: NotificationType,
    val recipient: IuvUser,
    val from: String? = null,
    val message: String,
    val dateSent: Date
)