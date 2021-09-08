package com.playground.notificationdemo.db

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date

@Entity
@TypeConverters(DateConverters::class)
@Keep
data class NotificationItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: Date,
    val title: String,
    val content: String,
    val raw: String
)