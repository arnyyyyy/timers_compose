package com.arno.timers_compose.feature_store_timers

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [TimerEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TimerDatabase : RoomDatabase() {
        abstract fun timerDao(): TimerDao

        companion object {
                @Volatile
                private var INSTANCE: TimerDatabase? = null

                fun getDatabase(context: Context): TimerDatabase {
                        return INSTANCE ?: synchronized(this) {
                                val instance = Room.databaseBuilder(
                                        context.applicationContext,
                                        TimerDatabase::class.java,
                                        "timer_database"
                                )
                                        .fallbackToDestructiveMigration(false)
                                        .build()
                                INSTANCE = instance
                                instance
                        }
                }
        }
}
