package mx.utng.smarthealthmonitor.tv

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class TvViewModel : ViewModel() {

    private val _fc = MutableStateFlow(0)
    val fc: StateFlow<Int> = _fc.asStateFlow()

    init {
        viewModelScope.launch {
            while (true) {
                try {
                    val bpm = consultarTelefono()
                    if (bpm != null) {
                        _fc.value = bpm

                        // OPCIONAL: Guardar la lectura en Room usando Dispatchers.IO
                        // para que obtenga un ID real autogenerado de la base de datos
                        /*
                        withContext(Dispatchers.IO) {
                            SmartHealthDB.getDatabase(contexto).lecturaDao().insertar(
                                LecturaFC(valorBpm = bpm, hora = "...")
                            )
                        }
                        */
                    }
                } catch (e: Exception) {
                    Log.e("TvViewModel", "Error consultando teléfono", e)
                }
                delay(3000)
            }
        }
    }

    private suspend fun consultarTelefono(): Int? = withContext(Dispatchers.IO) {
        val conn = URL("http://10.0.2.2:8888/fc").openConnection() as HttpURLConnection
        conn.connectTimeout = 3000
        conn.readTimeout = 3000
        val body = conn.inputStream.bufferedReader().use { it.readText() }
        Regex("\"fc\":(\\d+)").find(body)?.groupValues?.get(1)?.toIntOrNull()
    }
}