package com.playground.notificationdemo.repository

import androidx.lifecycle.LiveData
import com.playground.notificationdemo.db.NotificationItem

interface NotificationsRepository {
    val notifications: LiveData<List<NotificationItem>>
}