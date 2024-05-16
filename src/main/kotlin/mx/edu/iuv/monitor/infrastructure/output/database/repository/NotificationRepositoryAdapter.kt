package mx.edu.iuv.monitor.infrastructure.output.database.repository


import mx.edu.iuv.monitor.domain.const.NotificationReason
import mx.edu.iuv.monitor.domain.const.NotificationType
import mx.edu.iuv.monitor.domain.model.Notification
import mx.edu.iuv.monitor.domain.service.output.NotificationRepository
import mx.edu.iuv.monitor.infrastructure.output.database.aggregations.ReasonCount
import mx.edu.iuv.monitor.infrastructure.output.database.aggregations.UserReasonCount
import mx.edu.iuv.monitor.infrastructure.output.database.entity.NotificationLog
import mx.edu.iuv.monitor.infrastructure.output.database.entity.UserLog
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.Aggregation.group
import org.springframework.data.mongodb.core.aggregation.Aggregation.limit
import org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation
import org.springframework.data.mongodb.core.aggregation.Aggregation.sort
import org.springframework.data.mongodb.core.aggregation.Aggregation.unwind
import org.springframework.data.mongodb.core.aggregation.AggregationOperation
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.aggregation.GroupOperation
import org.springframework.data.mongodb.core.aggregation.LimitOperation
import org.springframework.data.mongodb.core.aggregation.SortOperation
import org.springframework.data.mongodb.core.aggregation.TypedAggregation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

@Repository
class NotificationRepositoryAdapter(
    private val mongoNotificationLogRepository: MongoNotificationLogRepository,
    private val mongoTemplate: MongoTemplate
) : NotificationRepository {

    override fun findById(id: String): Optional<NotificationLog> {
        return mongoNotificationLogRepository.findById(id)
    }

    override fun findByUserId(id: String): List<NotificationLog> {
        return mongoTemplate.query(NotificationLog::class.java)
            .matching(Query.query(Criteria.where("user._id").isEqualTo(id))).all()
    }

    override fun findByNotificationType(type: NotificationType): List<NotificationLog> {
        return mongoTemplate.query(NotificationLog::class.java)
            .matching(Query.query(Criteria.where("type").isEqualTo(type.name))).all()
    }

    override fun findByNotificationReason(reason: NotificationReason): List<NotificationLog> {
        return mongoTemplate.query(NotificationLog::class.java)
            .matching(Query.query(Criteria.where("reason").isEqualTo(reason.name))).all()
    }

    override fun getNotificationCountByUserId(id: String): Long {
        return mongoTemplate.query(NotificationLog::class.java)
            .matching(Query.query(Criteria.where("user._id").isEqualTo(id))).count()
    }

    override fun getReasonCount(reason: NotificationReason): Long {
        return mongoTemplate.query(NotificationLog::class.java)
            .matching(Query.query(Criteria.where("reason").isEqualTo(reason.name))).count()
    }

    override fun getAllReasonsCount(): List<Pair<NotificationReason, Long>> {
        val groupOperation: GroupOperation = group("reason").count().`as`("count")
        val aggregation: Aggregation = newAggregation(groupOperation)

        val mappedResults = mongoTemplate.aggregate<ReasonCount>(aggregation, COLLECTION_NAME_NOTIFICATIONS)
            .mappedResults

        return mappedResults.mapNotNull{ Pair(NotificationReason.valueOf(it.reason), it.count) }
    }

    override fun getMostNotifiedReason(): Pair<NotificationReason, Long> {
        val groupOperation: GroupOperation = group("reason").count().`as`("count")
        val sortOperation: SortOperation = sort(Sort.by(Sort.Order.desc("count")))
        val limitOperation: LimitOperation = limit(1)

        val aggregation: Aggregation = newAggregation(groupOperation, sortOperation, limitOperation)
        val mappedResults = mongoTemplate.aggregate<ReasonCount>(aggregation, COLLECTION_NAME_NOTIFICATIONS)
            .mappedResults

        return mappedResults.firstNotNullOf { Pair(NotificationReason.valueOf(it.reason), it.count) }
    }

    override fun getNotificationsSentLast24Hours(): List<NotificationLog> {
        val date24HoursAgo = Date.from(LocalDateTime.now().minusHours(24).toInstant(ZoneOffset.UTC))
        return mongoTemplate.query(NotificationLog::class.java)
            .matching(Query.query(Criteria.where("dateSent").gte(date24HoursAgo))).all()
    }

    override fun getNotificationsSentInTheLastHours(hours: Long): List<NotificationLog> {
        val hoursAgo = Date.from(LocalDateTime.now().minusHours(hours).toInstant(ZoneOffset.UTC))
        return mongoTemplate.query(NotificationLog::class.java)
            .matching(Query.query(Criteria.where("dateSent").gte(hoursAgo))).all()
    }

    override fun getNotificationsSentOn(date: LocalDate, strict: Boolean): List<NotificationLog> {
        val startDate = Date.from(date.atStartOfDay().toInstant(ZoneOffset.UTC))
        val endDate = Date.from(date.atStartOfDay().plusDays(1).toInstant(ZoneOffset.UTC))

        return mongoTemplate.find(
            Query.query(Criteria.where("dateSent").gte(startDate).lt(endDate)),
            NotificationLog::class.java
        )
    }


    override fun getUserIdMostNotified(): String {
        TODO("Not yet implemented")
    }

    override fun getUserIdWithMostNotifiedReason(): Pair<String, NotificationReason> {
        TODO("Not yet implemented")
    }

    override fun getUserIdWithMostNotifiedReasonLast24Hours(): Pair<String, NotificationReason> {
        val groupByUserId: GroupOperation = group("user._id").addToSet("reason").`as`("reasons")
        val unwindReasons: AggregationOperation = unwind("reasons")
        val groupByUserIdAndReason: GroupOperation = group("user._id", "reasons").count().`as`("count")
        val sortOperation: SortOperation = sort(Sort.by(Sort.Order.desc("count")))
        val limitOperation: LimitOperation = limit(1)


        //val groupByUserIdAgain: GroupOperation = group("userId").first("reason").`as`("highestReason").first("count").`as`("highestCount")

        val aggregation: TypedAggregation<UserReasonCount> = newAggregation(
            UserReasonCount::class.java,
            groupByUserId,
            unwindReasons,
            groupByUserIdAndReason,
            sortOperation,
            limitOperation
        )

        val mappedResults = mongoTemplate.aggregate(aggregation, UserReasonCount::class.java).mappedResults

        TODO()
    }

    override fun getAllUserIdsWithMostNotifiedReason(): List<Pair<String, NotificationReason>> {
        TODO("Not yet implemented")
    }

    override fun getUserWithMostNotifiedReason(): Pair<UserLog, NotificationReason> {
        TODO("Not yet implemented")
    }

    override fun getAllUsersWithMostNotifiedReason(): List<Pair<UserLog, NotificationReason>> {
        TODO("Not yet implemented")
    }

    override fun getUserLastNotification(userId: String): NotificationLog {
        TODO("Not yet implemented")
    }

    override fun getAllUsersLastNotification(): List<NotificationLog> {
        // TODO will need pagination
        TODO("Not yet implemented")
    }


    override fun saveNotification(notification: Notification){
        val user = notification.recipient.let { user ->
            user.courses.firstOrNull()?.let { course ->
                UserLog(
                    id = user.id.toString(),
                    firstname = user.firstName,
                    userRole = user.userRole,
                    courseId = course.id.toString(),
                    courseName = course.shortName,
                    lastPlatformAccess = user.lastPlatformAccess,
                    lastCourseAccess = course.lastAccessedOn
                )
            }
        }

        if (user == null){
            // TODO: Log notification not recorded because user data missing
            return
        }

        val notificationLog = NotificationLog(
            type = notification.type,
            reason = notification.reason,
            user = user,
            dateSent = Date()
        )

        mongoNotificationLogRepository.save(notificationLog)
    }

    override fun saveAllNotifications(notifications: List<Notification>){
        val notificationsToRecord = notifications.mapNotNull { notification ->
            notification.recipient.let { user ->
                // We assume only one course for now no need to do a mapping or each course
                val course = user.courses.firstOrNull()
                course?.run {
                    NotificationLog(
                        type = notification.type,
                        reason = notification.reason,
                        user = UserLog(
                            id = user.id.toString(),
                            firstname = user.firstName,
                            userRole = user.userRole,
                            courseId = course.id.toString(),
                            courseName = course.shortName,
                            lastPlatformAccess = user.lastPlatformAccess,
                            lastCourseAccess = course.lastAccessedOn
                        ),
                        dateSent = Date()
                    )
                }
            }
        }

        try {
            mongoNotificationLogRepository.saveAll(notificationsToRecord)
        } catch (e: Exception){
            // log exception
            throw Exception("Error while saving notifications", e)
        }

    }


    companion object {
        private const val COLLECTION_NAME_NOTIFICATIONS = "notifications"
    }

}