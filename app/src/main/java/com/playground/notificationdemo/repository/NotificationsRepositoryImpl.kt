package com.playground.notificationdemo.repository

import com.playground.notificationdemo.db.NotificationDao

class NotificationsRepositoryImpl(
    private val dao: NotificationDao
) : NotificationsRepository {
    override val notifications by lazy { dao.getAll() }
}