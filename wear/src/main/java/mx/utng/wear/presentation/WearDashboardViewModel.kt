package mx.utng.wear.presentation

import android.app.Application
import androidx.health.services.client.HealthServices
import androidx.health.services.client.PassiveListenerCallback
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.PassiveListenerConfig
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.health.services.client.clearPassiveListenerCallback
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mx.utng.shared.data.SmartHealthRepository
import mx.utng.shared.data.db.LecturaFC

class WearDashboardViewModel(app: Application) : AndroidViewModel(app) {

    private val _fc = MutableStateFlow(72)
    val fc: StateFlow<Int> = _fc.asStateFlow()

    private val passiveClient =
        HealthServices.getClient(app).passiveMonitoringClient

    private val callback = object : PassiveListenerCallback {
        override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
            // API pública en 1.1.0-alpha03+: getData() con el tipo específico
            val bpm = dataPoints
                .getData(DataType.HEART_RATE_BPM)  // List<SampleDataPoint<Double>>
                .lastOrNull()
                ?.value
                ?.toInt()
            if (bpm != null) _fc.value = bpm
        }
    }

    val historial: StateFlow<List<LecturaFC>> =
        SmartHealthRepository.obtenerHistorial()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    init {
        SmartHealthRepository.init(app)
        val config = PassiveListenerConfig.builder()
            .setDataTypes(setOf(DataType.HEART_RATE_BPM))
            .build()
        viewModelScope.launch {
            passiveClient.setPassiveListenerCallback(config, callback)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            passiveClient.clearPassiveListenerCallback()
        }
    }
}