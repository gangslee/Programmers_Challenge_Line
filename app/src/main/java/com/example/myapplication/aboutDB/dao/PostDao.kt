package com.example.myapplication.aboutDB.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.aboutDB.entity.ImgData
import com.example.myapplication.aboutDB.entity.WriteData

@Dao
interface PostDao {
    @Query("SELECT * FROM WriteData")
    fun getAllText() : List<WriteData>

    @Query("SELECT * FROM ImgData")
    fun getAllImg() : List<ImgData>

    @Insert
    fun insertText(writeData : WriteData)

    @Insert
    fun insertImg(imgData: ImgData)
}