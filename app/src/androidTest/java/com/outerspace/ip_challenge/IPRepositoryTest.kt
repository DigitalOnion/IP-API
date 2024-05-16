package com.outerspace.ip_challenge

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.outerspace.ip_challenge.data_layer.IPDao
import com.outerspace.ip_challenge.data_layer.IPEntity
import com.outerspace.ip_challenge.data_layer.IPRepository
import com.outerspace.ip_challenge.data_layer.IPRoomDB
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException

@RunWith(AndroidJUnit4::class)

class IPRepositoryTest {
    lateinit var db: IPRoomDB
    lateinit var dao: IPDao

    @Before
    fun initializeDB() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, IPRoomDB::class.java).build()
        dao = db.ipDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun dbInsertIPAndRetrieveTest() {
        runTest {
            val bunchOfIps = bunchOfIpEntities()
            bunchOfIps.forEach {
                Log.d("IP TEST", "Insert to table: ${it.country} ${it.city}")

                dao.insertIP(                                      // Test subject
                    it.pkIP,
                    it.status,
                    it.country,
                    it.countryCode,
                    it.region,
                    it.regionName,
                    it.city,
                    it.zip,
                    it.lat,
                    it.lon,
                    it.timezone,
                    it.isp,
                    it.org,
                    it.autonomousSystem,
                    )
            }

            val ipEntityFlow = dao.getAllIPs().take(count = 1)  // Test subject

            ipEntityFlow.collect {
                it.forEach {ipEntity ->
                    Log.d("IP TEST", "read from flow: ${ipEntity.country} ${ipEntity.city} @ ${ipEntity.timestamp}")
                    assertTrue(bunchOfIps.firstOrNull { ip ->
                        ipEntity.country == ip.country &&
                                ipEntity.city == ip.city } != null
                    )
                }
            }
        }
    }

    @Test
    fun repositoryInsertUnknownIPTest() {
        runTest {
            val dispatcher = StandardTestDispatcher(testScheduler)
            val scope = CoroutineScope(dispatcher)

            val repo = IPRepository()
            val id = "52.21.50.234"
            val ip: IPEntity = repo.getIPById(id, scope)           // Test subject

            Log.d("IP TEST", "requested unknown IP $id results ${ip.country}, ${ip.city}")
        }
    }

    @Test
    fun repositoryInsertAndRetrieveTest() {
        runTest {
            val dispatcher = StandardTestDispatcher(testScheduler)
            val scope = CoroutineScope(dispatcher)

            val repo = IPRepository()
            val bunchOfIps = bunchOfIpEntities()
            bunchOfIps.forEach {
                Log.d("IP TEST", "Insert to table: ${it.country} ${it.city}")

                repo.addIP(it)                                  // Test subject
            }

            val ids = bunchOfIps.map{ entity -> entity.pkIP }

            ids.forEach { id ->
                val ip: IPEntity = repo.getIPById(id, scope)           // Test subject

                Log.d("IP TEST", "$id retrieved: ${ip.country} ${ip.city}")
                assertTrue(bunchOfIps.firstOrNull { ipF ->
                    ip.country == ipF.country &&
                    ip.city == ipF.city } != null
                )
            }
        }
    }
}
