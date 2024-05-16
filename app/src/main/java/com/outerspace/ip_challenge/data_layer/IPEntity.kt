package com.outerspace.ip_challenge.data_layer

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

@Entity(tableName = "ip_addresses")
data class IPEntity(
    @PrimaryKey val pkIP: String,
    @ColumnInfo(name = "status") val status: String?,
    @ColumnInfo(name = "country") val country: String?,
    @ColumnInfo(name = "countryCode") val countryCode: String?,
    @ColumnInfo(name = "region") val region: String?,
    @ColumnInfo(name = "regionName") val regionName: String?,
    @ColumnInfo(name = "city") val city: String?,
    @ColumnInfo(name = "zip") val zip: String?,
    @ColumnInfo(name = "lat") val lat: Double?,
    @ColumnInfo(name = "lon") val lon: Double?,
    @ColumnInfo(name = "timezone") val timezone: String?,
    @ColumnInfo(name = "isp") val isp: String?,
    @ColumnInfo(name = "org") val org: String?,
    @ColumnInfo(name = "autonomousSystem") val autonomousSystem: String?,

    @ColumnInfo(name = "timestamp", defaultValue = "CURRENT_TIMESTAMP") val timestamp: ZonedDateTime?,
)

