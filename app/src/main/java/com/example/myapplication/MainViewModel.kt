package com.example.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.myapplication.aboutDB.AppDatabase
import com.example.myapplication.aboutDB.PostData

class MainViewModel(application : Application) : AndroidViewModel(application){
    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "PostDB"
    ).build()

    fun getAll(): LiveData<List<PostData>> {
        return db.postDao().getAll()
    }

    suspend fun insert(postData: PostData){
        db.postDao().insert(postData)
    }

    suspend fun delete(id: Int){
        db.postDao().delete(id)
    }
}