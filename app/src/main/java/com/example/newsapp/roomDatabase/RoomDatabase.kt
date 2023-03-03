package com.example.newsapp.roomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.example.newsapp.models.News


@Database(entities = [News::class], version = 1, exportSchema = false)
abstract class RoomDatabase : androidx.room.RoomDatabase() {


    abstract fun daoDatabase(): Dao

    companion object {
        @Volatile
        private var INSTANCE: RoomDatabase? = null
        fun getDatabase(context: Context): RoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }

        }


    }
}