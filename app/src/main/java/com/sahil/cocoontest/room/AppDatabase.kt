package com.sahil.cocoontest.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sahil.cocoontest.BuildConfig
import com.sahil.cocoontest.models.localdb.NewsTable

@Database(entities = [NewsTable::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun topStoriesDao(): NewsDao

    companion object {

        private var db: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (db == null) {
                db = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, BuildConfig.DB_NAME)
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return db!!
        }
    }
}