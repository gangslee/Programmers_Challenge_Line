package com.example.myapplication.aboutDB

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class Converters {
    @TypeConverter
    fun stringToList(value: String?): ArrayList<String>? {
        val listType: Type = object : TypeToken<ArrayList<String>>(){}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun listToString(list: ArrayList<String>?): String? {
        val gson = Gson()
        val json = gson.toJson(list)
        return json
    }
}
