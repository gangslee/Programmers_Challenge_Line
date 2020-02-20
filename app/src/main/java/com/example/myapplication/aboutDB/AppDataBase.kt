package com.example.myapplication.aboutDB

import androidx.room.*

@Database(entities = [PostData::class], version = 1)
//@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}