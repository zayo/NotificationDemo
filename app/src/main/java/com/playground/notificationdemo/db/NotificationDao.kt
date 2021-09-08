package com.playground.notificationdemo.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notificationItem: NotificationItem)

    @Query("SELECT * FROM NotificationItem WHERE id = :id")
    fun get(id: Long): NotificationItem?

    @Query("SELECT * FROM NotificationItem")
    fun getAll(): LiveData<List<NotificationItem>>

    @Query("SELECT COUNT(*) FROM NotificationItem")
    suspend fun count() : Long

    @Query("DELETE FROM NotificationItem")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(notificationItem: NotificationItem)
}