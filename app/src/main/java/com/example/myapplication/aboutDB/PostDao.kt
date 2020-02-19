package com.example.myapplication.aboutDB

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myapplication.aboutDB.PostData

@Dao
interface PostDao {
    @Query("SELECT * FROM PostData")
    fun getAll() : LiveData<List<PostData>>

    @Insert
    fun insert(postData: PostData)

    @Update
    fun update(postData: PostData)

    @Query("Delete FROM PostData WHERE id = :id")
    fun delete(id : Int)
}