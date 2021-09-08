package com.playground.notificationdemo.db

import androidx.room.TypeConverter
import java.util.Date

class DateConverters {

    @TypeConverter
    fun to(long: Long): Date = Date(long)

    @TypeConverter
    fun from(date: Date): Long = date.time
}