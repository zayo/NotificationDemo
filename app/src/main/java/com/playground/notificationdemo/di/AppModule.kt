package com.playground.notificationdemo

import androidx.room.Room
import com.playground.notificationdemo.db.AppDatabase
import com.playground.notificationdemo.repository.NotificationsRepository
import com.playground.notificationdemo.repository.NotificationsRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module

fun createAppModule() : Module = module {
    single { Room.databaseBuilder(get(), AppDatabase::class.java, "database-name").build() }
    single { get<AppDatabase>().notificationsDao() }
    single<NotificationsRepository> { NotificationsRepositoryImpl(get()) }
    factory { get<NotificationsRepository>().notifications}
}