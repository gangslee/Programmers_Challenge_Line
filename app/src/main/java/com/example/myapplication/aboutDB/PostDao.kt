package com.example.myapplication.aboutDB

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PostDao {
    @Query("SELECT * FROM PostData")
    fun getAll() : LiveData<List<PostData>>

    @Insert
    fun insert(postData: PostData)

    @Query("UPDATE PostData SET title = :title, content = :content, imgList = :imgList, date = :date WHERE id = :id")
    fun update(title: String, content: String, imgList: String, date: String, id: Int)

    @Query("Delete FROM PostData WHERE id = :id")
    fun delete(id : Int)
}