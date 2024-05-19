package com.outerspace.ip_challenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import com.outerspace.ip_challenge.data_layer.IPEntity
import com.outerspace.ip_challenge.databinding.ActivityMainBinding
import com.outerspace.ip_challenge.ui_layer.IPViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ipViewModelFactory = IPViewModel.Factory(this as LifecycleOwner)
        val viewModel = ViewModelProvider(this as ViewModelStoreOwner, ipViewModelFactory)[IPViewModel::class]

        binding = DataBindingUtil
            .setContentView(this, R.layout.activity_main)

        binding.ipViewModel = viewModel

        binding.searchButton.setOnClickListener {
            val ipStr = binding.ipAddressInput.text.toString()
            lifecycleScope.launch {
                viewModel.evaluateIpAddress(ipStr, lifecycleScope)
            }
        }

        viewModel.ipEntityListener = {ipEntity ->
            lifecycleScope.launch {
                val unencodedHtml = formatIPEntity(ipEntity)
                val encodedHtml: String = Base64.encodeToString(unencodedHtml.toByteArray(), Base64.NO_PADDING)
                binding.output.loadData(encodedHtml, "text/html", "base64")
            }
        }
    }

    private val TABLE_HEAD = "<table>"
    private val LINE_START = "<tr><td>"
    private val SEPARATOR = "</td><td><b>"
    private val LINE_END = "</b></td></tr>"
    private val TABLE_FOOT = "</table>"

    private fun formatIPEntity(ip: IPEntity): String {
        val sb = StringBuilder()
        sb.append(TABLE_HEAD)
        sb.append(LINE_START).append("status").append(SEPARATOR).append(ip.status).append(LINE_END)
        sb.append(LINE_START).append("country").append(SEPARATOR).append(ip.country).append(LINE_END)
        sb.append(LINE_START).append("countryCode").append(SEPARATOR).append(ip.countryCode).append(LINE_END)
        sb.append(LINE_START).append("region").append(SEPARATOR).append(ip.region).append(LINE_END)
        sb.append(LINE_START).append("regionName").append(SEPARATOR).append(ip.regionName).append(LINE_END)
        sb.append(LINE_START).append("city").append(SEPARATOR).append(ip.city).append(LINE_END)
        sb.append(LINE_START).append("zip").append(SEPARATOR).append(ip.zip).append(LINE_END)
        sb.append(LINE_START).append("lat").append(SEPARATOR).append(ip.lat).append(LINE_END)
        sb.append(LINE_START).append("lon").append(SEPARATOR).append(ip.lon).append(LINE_END)
        sb.append(LINE_START).append("timezone").append(SEPARATOR).append(ip.timezone).append(LINE_END)
        sb.append(LINE_START).append("isp").append(SEPARATOR).append(ip.isp).append(LINE_END)
        sb.append(LINE_START).append("org").append(SEPARATOR).append(ip.org).append(LINE_END)
        sb.append(LINE_START).append("autonomousSystem").append(SEPARATOR).append(ip.autonomousSystem).append(LINE_END)
        sb.append(TABLE_FOOT)
        return sb.toString()
    }

}