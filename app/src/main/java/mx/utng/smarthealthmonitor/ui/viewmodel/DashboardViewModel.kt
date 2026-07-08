package mx.utng.smarthealthmonitor.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.*
import mx.utng.shared.data.SmartHealthRepository
import androidx.lifecycle.viewModelScope
import mx.utng.shared.data.db.LecturaFC
class DashboardViewModel : ViewModel() {
    val fc: StateFlow<Int> = SmartHealthRepository.fcFlow

    val pasos: StateFlow<Int> = SmartHealthRepository.pasosFlow

    val historial: StateFlow<List<LecturaFC>> = SmartHealthRepository
        .obtenerHistorial()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}