package com.example.myapplication.aboutDB.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class WriteData (
    var title : String,
    var content : String,
    @PrimaryKey var date : String
)

