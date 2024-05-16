package com.outerspace.ip_challenge.data_layer

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

const val CURRENT_DATABASE_VERSION = 1

@Dao
interface IPDao {
    @Query("insert into ip_addresses (pkIP, status, country, countryCode, region, regionName, city, zip, lat, lon, timezone, isp, org, autonomousSystem) values (:pkIP, :status, :country, :countryCode, :region, :regionName, :city, :zip, :lat, :lon, :timezone, :isp, :org, :autonomousSystem)")
    suspend fun insertIP(pkIP: String?,
                         status: String?,
                         country: String?,
                         countryCode: String?,
                         region: String?,
                         regionName: String?,
                         city: String?,
                         zip: String?,
                         lat: Double?,
                         lon: Double?,
                         timezone: String?,
                         isp: String?,
                         org: String?,
                         autonomousSystem: String?)

    @Update(entity= IPEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateIP(ip: IPEntity)

    @Query("select * from ip_addresses where pkIp=:ipID")
    suspend fun getIpById(ipID: String): IPEntity

    @Query("select * from ip_addresses")
    fun getAllIPs(): Flow<List<IPEntity>>
}

@Database(entities = [IPEntity::class], version = CURRENT_DATABASE_VERSION, exportSchema = false)
@TypeConverters(Converters::class)
abstract class IPRoomDB: RoomDatabase() {
    abstract fun ipDao(): IPDao
}


