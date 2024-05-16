package com.outerspace.ip_challenge.data_layer

import androidx.room.Room
import com.outerspace.ip_challenge.IPChallengeApplication
import com.outerspace.ip_challenge.network_layer.IPClient
import com.outerspace.ip_challenge.network_layer.IPSchema
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

const val DB_NAME = "IP_Challenge_Database"

class IPRepository {
    private val context = IPChallengeApplication.appContext()
    private val db = Room.databaseBuilder(context, IPRoomDB::class.java, DB_NAME).build()
    private val dao = db.ipDao()

    fun IPSchema.toIpEntity(): IPEntity {
        return IPEntity(
            this.query ?: "",
            this.status,
            this.country,
            this.countryCode,
            this.region,
            this.regionName,
            this.city,
            this.zip,
            this.lat,
            this.lon,
            this.timezone,
            this.isp,
            this.org,
            this.autonomousSystem,
            null,
        )
    }

    suspend fun addIP(ip: IPEntity) = dao.insertIP(ip.pkIP, ip.status, ip.country, ip.countryCode, ip.region, ip.regionName, ip.city, ip.zip, ip.lat, ip.lon, ip.timezone, ip.isp, ip.org, ip.autonomousSystem)

    suspend fun replaceIP(ip: IPEntity) = dao.updateIP(ip)

    suspend fun getIPById(ipId: String, scope: CoroutineScope): IPEntity {
        val result: IPEntity = dao.getIpById(ipId)
        return if (result == null) {
            val client = IPClient()
            val d: Deferred<IPSchema> = scope.async(Dispatchers.IO) { client.getIPSchema(ipId) }
            val schema: IPSchema = d.await()
            val entity = schema.toIpEntity()
            addIP(entity)
            entity
        } else {
            result
        }
    }
}