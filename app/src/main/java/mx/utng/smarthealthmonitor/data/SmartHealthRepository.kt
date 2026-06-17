package mx.utng.smarthealthmonitor.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.Flow
import mx.utng.smarthealthmonitor.data.db.LecturaFCDao
import android.content.Context
import mx.utng.smarthealthmonitor.data.db.LecturaFC
import mx.utng.smarthealthmonitor.data.db.SmartHealthDB
import kotlinx.coroutines.flow.emptyFlow
/**
 *	Repositorio singleton que centraliza los datos de salud.
 *	El WearListenerService escribe aquí.
 *	El ViewModel lee de aquí.
 */
object SmartHealthRepository {
    private val _fcFlow = MutableStateFlow(0)
    val fcFlow: StateFlow<Int> = _fcFlow.asStateFlow()
    private var dao: LecturaFCDao? = null
    fun init(context: Context) {
        dao = SmartHealthDB.getDatabase(context).lecturaDao()
    }
    suspend fun actualizarFC(bpm: Int) {
        _fcFlow.value = bpm
// Persistir en Room automáticamente
        dao?.insertar(LecturaFC(valorBpm = bpm))
    }
    // Flow del historial desde Room
    fun obtenerHistorial(): Flow<List<LecturaFC>> =
    dao?.obtenerUltimas() ?: emptyFlow()
}