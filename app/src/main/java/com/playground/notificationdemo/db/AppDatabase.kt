package com.playground.notificationdemo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [NotificationItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notificationsDao(): NotificationDao
}