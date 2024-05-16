package mx.edu.iuv.monitor.infrastructure.output.database.repository

import mx.edu.iuv.monitor.infrastructure.output.database.entity.NotificationLog
import org.springframework.data.mongodb.repository.MongoRepository

interface MongoNotificationLogRepository: MongoRepository<NotificationLog, String>