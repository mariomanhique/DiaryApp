package com.mariomanhique.util

import android.annotation.SuppressLint
//import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date

object DateConverter {
//
//    @TypeConverter
//    fun timeStampFromDate(date: Date):Long{
//        return date.time
//    }
//
//    @TypeConverter
//    fun dateTimeStamp(timestamp:Long): Date?{
//        return Date(timestamp)
//    }

    @SuppressLint("SimpleDateFormat")
    fun formatDate(instant: Instant):String{
        val sdf =  SimpleDateFormat("d MMM y, hh:mm a")
        val date = Date.from(instant)

        return sdf.format(date)
    }
}