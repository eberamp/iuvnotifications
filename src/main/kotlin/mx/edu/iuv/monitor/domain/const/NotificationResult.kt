package mx.edu.iuv.monitor.domain.const

import mx.edu.iuv.monitor.domain.model.Notification

sealed class NotificationResult {

    data class Success(val message: String, val notifications: List<Notification>) : NotificationResult() {
        constructor(message: String, notification: Notification): this(message, listOf(notification))
    }

    data class Failure(val errorMessage: String, val notifications: List<Notification>) : NotificationResult() {
        constructor(errorMessage: String, notification: Notification): this(errorMessage, listOf(notification))
    }

}