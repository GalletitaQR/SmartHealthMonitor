package mx.utng.wear.presentation

import androidx.health.services.client.PassiveListenerService
import androidx.health.services.client.data.*
import kotlinx.coroutines.*
import android.content.Context
import androidx.health.services.client.HealthServices
import kotlinx.coroutines.guava.await //no se si es guava
import android.util.Log

class HealthDataService : PassiveListenerService() {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private lateinit var wearDataSender: WearDataSender
    override fun onCreate() {
        super.onCreate()
        Log.d("HealthDataService", "SERVICE CREADO ✓")
        wearDataSender = WearDataSender(this) // S6: MessageClient
    }
    override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
        val fcDataPoints = dataPoints.getData(DataType.HEART_RATE_BPM)
        Log.d("HealthDataService", "DATOS RECIBIDOS ✓")
        fcDataPoints.forEach { dataPoint ->
            if (dataPoint is SampleDataPoint<Double>) {
                val bpm = dataPoint.value.toInt()
                scope.launch { wearDataSender.enviarFC(bpm) }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
    companion object {
        suspend fun registrar(context: Context) {
            val hsClient = HealthServices.getClient(context)
            val passiveClient = hsClient.passiveMonitoringClient
            val config = PassiveListenerConfig.builder()
                .setDataTypes(setOf(DataType.HEART_RATE_BPM))
                .setShouldUserActivityInfoBeRequested(true)
                .build()
            passiveClient.setPassiveListenerServiceAsync(
                HealthDataService::class.java,
                config
            ).await()
        }
    }
}