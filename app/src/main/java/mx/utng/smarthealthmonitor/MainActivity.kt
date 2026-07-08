package mx.utng.smarthealthmonitor

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import mx.utng.shared.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.navigation.SmartHealthNavGraph
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity(), MessageClient.OnMessageReceivedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartHealthNavGraph()
        }
    }

    override fun onResume() {
        super.onResume()
        // Registrar el escuchador dinámico cuando la app está abierta
        Wearable.getMessageClient(this).addListener(this)
        Log.d("MainActivity", "Escuchador de Wearable registrado de forma dinámica")
    }

    override fun onPause() {
        super.onPause()
        // Removerlo para evitar fugas de memoria
        Wearable.getMessageClient(this).removeListener(this)
    }

    // Este método atrapará los datos directamente sin depender del Manifest en modo Debug

    override fun onMessageReceived(messageEvent: MessageEvent) {
        val data = String(messageEvent.data)
        val path = messageEvent.path

        when (path) {
            "/smarthealthmonitor/fc" -> {
                val bpm = data.toIntOrNull() ?: return
                lifecycleScope.launch { SmartHealthRepository.actualizarFC(bpm) }  // ← launch
            }
        }
    }
}