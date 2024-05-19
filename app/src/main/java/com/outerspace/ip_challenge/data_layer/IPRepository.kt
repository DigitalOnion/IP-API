package com.outerspace.ip_challenge.data_layer

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.outerspace.ip_challenge.IPChallengeApplication
import com.outerspace.ip_challenge.network_layer.IPClient
import com.outerspace.ip_challenge.network_layer.IPSchema
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime

const val DB_NAME = "IP_Challenge_Database"

const val EXPIRE_MILLS: Long = 1000L * 60L * 5L         // 5 minutes in milliseconds
const val FAIL_STATUS = "fail"

class IPRepository(val owner: LifecycleOwner) {
    private val context = IPChallengeApplication.appContext()
    private val db = Room.databaseBuilder(context, IPRoomDB::class.java, DB_NAME).build()
    private val dao = db.ipDao()

    private val mutableIpEntity = MutableLiveData<IPEntity>()

    var onAcquireIpEntity: (IPEntity) -> Unit = {}
        set(callback) {
            field = callback
            mutableIpEntity.observe(owner, callback)
        }

    suspend fun evaluateIpAddress(ipAddress: String, scope: CoroutineScope) {
        val ipEntity = getIPById(ipAddress, scope)
        mutableIpEntity.value = ipEntity
    }

    fun IPSchema.toIpEntity(): IPEntity {
        return IPEntity(
            this.query ?: "", this.status, this.country, this.countryCode,
            this.region, this.regionName, this.city, this.zip, this.lat,
            this.lon, this.timezone, this.isp, this.org, this.autonomousSystem
        )
    }

    suspend fun addIP(ip: IPEntity) = dao.insertIP(ip.pkIP, ip.status, ip.country, ip.countryCode,
        ip.region, ip.regionName, ip.city, ip.zip, ip.lat, ip.lon,
        ip.timezone, ip.isp, ip.org, ip.autonomousSystem)

    suspend fun replaceIP(ip: IPEntity) = dao.updateIP(ip)

    suspend fun getIPById(ipId: String, scope: CoroutineScope): IPEntity {
        val result: IPEntity? = if (ipId.isNotEmpty())
            dao.getIpById(ipId)
        else
            IPEntity.FAIL
        val ip = if (result == null) {
            val entity = fetch(ipId, scope)
            addIP(entity)
            entity
        } else if ( result.status == FAIL_STATUS) {
            val entity = fetch("", scope)
            replaceIP(entity)
            entity
        } else if ( timestampIsExpired(result.timestamp, EXPIRE_MILLS) ) {
            val entity = fetch(ipId, scope)
            replaceIP(entity)
            entity
        } else {
            result
        }
        Log.d("TEST", "IPEntity: ${ip.city}, ${ip.country} @ ${ip.timestamp}")
        return ip
    }

    private suspend fun fetch(ipId: String, scope: CoroutineScope): IPEntity {
        val client = IPClient()
        val d: Deferred<IPSchema> = scope.async(Dispatchers.IO) {
            client.fetchIPSchema(ipId)
        }
        val schema: IPSchema = d.await()
        return schema.toIpEntity()
    }

    private fun timestampIsExpired(timestamp: ZonedDateTime?, expiration: Long): Boolean {
        val mills: Long = timestamp?.toInstant()?.toEpochMilli() ?: 0L
        val now: Long = System.currentTimeMillis()
        Log.d("TEST", "timestamp: now-mills = ${now - mills} > ${expiration}")
        return now - mills > expiration
    }
}