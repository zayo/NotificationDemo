package com.playground.notificationdemo.service

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.playground.notificationdemo.db.NotificationDao
import com.playground.notificationdemo.db.NotificationItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.Date

class NotificationListener : NotificationListenerService(), CoroutineScope by MainScope() {

    private val dao: NotificationDao by inject()

    override fun onListenerConnected() {
        launch(Dispatchers.IO) {
            if (dao.count() == 0L) {
                activeNotifications.forEach(::onNotificationPosted)
            }
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        launch(Dispatchers.IO) {
            val title = "TITLE = ${sbn.notification.extras.getCharSequence(Notification.EXTRA_TITLE)}\n" +
                    "TITLE.BIG = ${sbn.notification.extras.getCharSequence(Notification.EXTRA_TITLE_BIG)}"
            val content = "CONTENT = ${sbn.notification.extras.getCharSequence(Notification.EXTRA_TEXT)}\n" +
                    "CONTENT.BIG = ${sbn.notification.extras.getCharSequence(Notification.EXTRA_BIG_TEXT)}"
            val raw = "EXTRAS = ${sbn.notification.extras}"
            val notification = NotificationItem(date = Date(sbn.postTime), title = title, content = content, raw = raw)
            dao.insert(notification)
        }
    }
}