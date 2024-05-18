package com.outerspace.ip_challenge.ui_layer

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.outerspace.ip_challenge.data_layer.IPEntity
import com.outerspace.ip_challenge.data_layer.IPRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IPViewModel(private val owner: LifecycleOwner): ViewModel() {
    // influx from UI to Data
    val mutableIpAddress: MutableLiveData<String> = MutableLiveData()

    // influx from Data Layer to UI
    val mutableIpEntity: MutableLiveData<IPEntity> = MutableLiveData()

    class Factory(
        private val lifecycleOwner: LifecycleOwner,
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return IPViewModel(lifecycleOwner) as T
        }
    }

    init {
        mutableIpAddress.observe(owner) {                   // query an IP address (most likely by the UI)
            viewModelScope.launch(Dispatchers.IO) {
                val ip: String = mutableIpAddress.value ?: ""
                val entity: IPEntity = IPRepository().getIPById(ip, viewModelScope)
                withContext(Dispatchers.Main) {
                    mutableIpEntity.value = entity
                }
            }
        }
    }
}
