package com.example.myapplication.aboutDB

import androidx.room.*
import com.example.myapplication.aboutDB.dao.PostDao
import com.example.myapplication.aboutDB.entity.ImgData
import com.example.myapplication.aboutDB.entity.WriteData

@Database(entities = [ImgData::class, WriteData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}