package com.example.myapplication.aboutDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class PostData (
    var title : String,
    var content : String,
    var imgList : String,
    var date : String
){
    @PrimaryKey(autoGenerate = true) var id : Int = 0
}

