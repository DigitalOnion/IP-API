package com.outerspace.ip_challenge.data_layer

import androidx.room.TypeConverter
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class Converters {
    @TypeConverter
    fun TimestampToDate(value: Long?): ZonedDateTime? {
        return if (value == null) null
        else ZonedDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault())
    }

    @TypeConverter
    fun dateToTimestamp(dateTime: ZonedDateTime?): Long? {
        return dateTime?.toInstant()?.toEpochMilli()
    }
}
