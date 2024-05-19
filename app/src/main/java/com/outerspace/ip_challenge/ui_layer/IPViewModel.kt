package com.outerspace.ip_challenge.ui_layer

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.outerspace.ip_challenge.data_layer.IPEntity
import com.outerspace.ip_challenge.data_layer.IPRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IPViewModel(private val owner: LifecycleOwner): ViewModel() {
    // Repository
    private val ipRepository: IPRepository = IPRepository(owner)

    // influx from UI to Data
    suspend fun evaluateIpAddress(ipAddress: String, scope: CoroutineScope) {
        ipRepository.evaluateIpAddress(ipAddress, scope)
    }

    // efflux from Data Layer to UI
    private val mutableIpEntity: MutableLiveData<IPEntity> = MutableLiveData()

    init {
        ipRepository.onAcquireIpEntity = { ipEntity ->
            mutableIpEntity.value = ipEntity
        }
    }

    var ipEntityListener: (IPEntity) -> Unit = {}
        set(listener) {
            mutableIpEntity.observe(owner, listener)
        }

    class Factory(
        private val lifecycleOwner: LifecycleOwner,
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return IPViewModel(lifecycleOwner) as T
        }
    }

}
