package com.outerspace.ip_challenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.outerspace.ip_challenge.databinding.ActivityMainBinding
import com.outerspace.ip_challenge.ui_layer.IPViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: IPViewModel = IPViewModel(this as LifecycleOwner)     // TODO: implement a factory and create from ViewModelProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil
            .setContentView(this, R.layout.activity_main)

        binding.ipViewModel = viewModel

        binding.searchButton.setOnClickListener {
            viewModel.mutableIpAddress.value = binding.ipAddressInput.text.toString()
        }

        viewModel.mutableIpEntity.observe(this as LifecycleOwner) { ipEntity ->
            lifecycleScope.launch {
                binding.output.text = ipEntity.city
            }
        }
    }

}