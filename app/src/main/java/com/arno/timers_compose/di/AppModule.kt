package com.arno.timers_compose.di

import android.content.Context
import com.arno.timers_compose.feature_firestore_sync.FirestoreSyncManager
import com.arno.timers_compose.feature_firestore_sync.FirestoreSyncRepository
import com.arno.timers_compose.feature_store_timers.TimerDao
import com.arno.timers_compose.feature_store_timers.TimerDatabase
import com.arno.timers_compose.feature_store_timers.TimerRepository
import com.arno.timers_compose.feature_store_timers.TimersContainer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

        @Provides
        @Singleton
        fun provideTimerDatabase(@ApplicationContext context: Context): TimerDatabase =
                TimerDatabase.getDatabase(context)

        @Provides
        @Singleton
        fun provideTimerDao(db: TimerDatabase): TimerDao = db.timerDao()

        @Provides
        @Singleton
        fun provideTimerRepository(dao: TimerDao): TimerRepository = TimerRepository(dao)

        @Provides
        @Singleton
        fun provideFirestoreSyncRepository(): FirestoreSyncRepository = FirestoreSyncRepository()

        @Provides
        @Singleton
        fun provideFirestoreSyncManager(
                timerRepository: TimerRepository,
                firestoreSyncRepository: FirestoreSyncRepository
        ): FirestoreSyncManager = FirestoreSyncManager(timerRepository, firestoreSyncRepository)

        @Provides
        @Singleton
        fun provideTimersContainer(
                timerRepository: TimerRepository,
                firestoreSyncManager: FirestoreSyncManager
        ): TimersContainer = object : TimersContainer {
                override val timerRepository: TimerRepository = timerRepository
                override val firestoreSyncManager: FirestoreSyncManager = firestoreSyncManager
        }
}

