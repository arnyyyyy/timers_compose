package com.arno.timers_compose.feature_store_timers

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [TimerEntity::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TimerDatabase : RoomDatabase() {
        abstract fun timerDao(): TimerDao

        companion object {
                @Volatile
                private var INSTANCE: TimerDatabase? = null

                private val MIGRATION_1_2 = object : Migration(1, 2) {
                        override fun migrate(db: SupportSQLiteDatabase) {
                                db.execSQL("ALTER TABLE timers ADD COLUMN category TEXT NOT NULL DEFAULT 'OTHER'")
                        }
                }

                fun getDatabase(context: Context): TimerDatabase {
                        return INSTANCE ?: synchronized(this) {
                                val instance = Room.databaseBuilder(
                                        context.applicationContext,
                                        TimerDatabase::class.java,
                                        "timer_database"
                                )
                                        .addMigrations(MIGRATION_1_2)
                                        .build()
                                INSTANCE = instance
                                instance
                        }
                }
        }
}
