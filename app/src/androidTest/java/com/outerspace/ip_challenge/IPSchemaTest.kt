package com.outerspace.ip_challenge

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.outerspace.ip_challenge.network_layer.IPClient
import com.outerspace.ip_challenge.network_layer.IPSchema
import kotlinx.coroutines.test.runTest

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

const val TESTTAG = "IPSchemaTest"

@RunWith(AndroidJUnit4::class)
class IPSchemaTest {
    @Test
    fun ipSchemaTest() {
        runTest {
            val ip = "24.48.0.1"
            val schema: IPSchema = IPClient().getIPSchema(ip)

            Log.d(TESTTAG, "Response Schema: ${schema.city}")
        }
    }
}