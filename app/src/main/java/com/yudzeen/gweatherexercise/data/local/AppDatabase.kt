package com.yudzeen.gweatherexercise.data.local

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yudzeen.gweatherexercise.data.local.dao.UserDao
import com.yudzeen.gweatherexercise.data.local.dao.WeatherDao
import com.yudzeen.gweatherexercise.data.local.entity.UserEntity
import com.yudzeen.gweatherexercise.data.local.entity.WeatherEntity

@Database(
    entities = [UserEntity::class, WeatherEntity::class],
    version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2)],
    exportSchema = true
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun weatherDao(): WeatherDao

    companion object {

        const val DATABASE_NAME = "gweather-db"

        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }

}