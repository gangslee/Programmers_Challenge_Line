package com.example.myapplication.aboutDB.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ImgData(

    var location : String,
    var date : String
){
    @PrimaryKey(autoGenerate = true) var id : Int = 0
}
