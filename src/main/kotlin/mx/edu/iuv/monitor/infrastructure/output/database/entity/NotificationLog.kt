package mx.edu.iuv.monitor.infrastructure.output.database.entity

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import mx.edu.iuv.monitor.domain.const.NotificationReason
import mx.edu.iuv.monitor.domain.const.NotificationType
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("notifications")
data class NotificationLog (

    @Id
    @JsonSerialize(using= ToStringSerializer::class)
    val id: ObjectId = ObjectId(),

    val type: NotificationType,
    val reason: NotificationReason,
    val user: UserLog,
    val dateSent: Date = Date()

)

data class UserLog (

    val id: String,
    val userRole: String,
    val firstname: String,
    val courseId: String,
    val courseName: String,
    val activityItemId: String = "",
    val activityItemName: String = "",
    val lastPlatformAccess: Date,
    val lastCourseAccess: Date,

)