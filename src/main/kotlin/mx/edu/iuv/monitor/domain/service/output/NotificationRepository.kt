package mx.edu.iuv.monitor.domain.service.output

import mx.edu.iuv.monitor.domain.const.NotificationReason
import mx.edu.iuv.monitor.domain.const.NotificationType
import mx.edu.iuv.monitor.domain.model.Notification
import mx.edu.iuv.monitor.infrastructure.output.database.entity.NotificationLog
import mx.edu.iuv.monitor.infrastructure.output.database.entity.UserLog
import java.time.LocalDate
import java.util.*

interface NotificationRepository {

    fun findById(id: String): Optional<NotificationLog>
    fun findByUserId(id: String): List<NotificationLog>
    fun findAll(): List<NotificationLog>

    fun findAllWeeksAgo(weeksAgo: Int): List<NotificationLog>
    fun findAllSentLast24Hours(): List<NotificationLog>
    fun findAllSentHoursAgo(hours: Long): List<NotificationLog>
    fun findAllSentDaysAgo(days: Long): List<NotificationLog>
    fun findAllSentOn(date: LocalDate, strict: Boolean = false): List<NotificationLog>

    fun findByNotificationType(type: NotificationType): List<NotificationLog>
    fun findByNotificationReason(reason: NotificationReason): List<NotificationLog>

    fun getNotificationCountByUserId(id: String): Long
    fun getReasonCount(reason: NotificationReason): Long
    fun getAllReasonsCount(): List<Pair<NotificationReason, Long>>
    fun getMostNotifiedReason(): Pair<NotificationReason, Long>

    fun getUserIdMostNotified(): String
    fun getUserIdWithMostNotifiedReason(): Pair<String, NotificationReason>
    fun getUserIdWithMostNotifiedReasonLast24Hours(): Pair<String, NotificationReason>
    fun getAllUserIdsWithMostNotifiedReason(): List<Pair<String, NotificationReason>>
    fun getUserWithMostNotifiedReason(): Pair<UserLog, NotificationReason>
    fun getAllUsersWithMostNotifiedReason(): List<Pair<UserLog, NotificationReason>>

    fun getUserLastNotification(userId: String): NotificationLog
    fun getAllUsersLastNotification(): List<NotificationLog>

    fun saveNotification(notification: Notification)
    fun saveAllNotifications(notifications: List<Notification>)

}