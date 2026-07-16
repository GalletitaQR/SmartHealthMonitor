package mx.utng.smarthealthmonitor.tv

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mx.utng.shared.data.remote.toLecturaFC
import androidx.lifecycle.ViewModelProvider

class TvViewModel(private val context: Context) : ViewModel() {
    private val neonRepo = TvNeonRepository()
    private val _state = MutableStateFlow(TvUiState())
    val state: StateFlow<TvUiState> = _state.asStateFlow()

    init {
        cargarDatos()
    }

    fun cargarDatos() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val lecturas = neonRepo.obtenerHistorialCompleto(50)
                val stats = neonRepo.obtenerEstadisticas()
                val alertas = neonRepo.obtenerAlertas24Horas()
                _state.update {
                    it.copy(
                        lecturas = lecturas.map { dto -> dto.toLecturaFC() },
                        estadisticas = stats.map { dto -> dto.toLecturaFC() },
                        alertas = alertas.map { dto -> dto.toLecturaFC() },
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun refresh() = cargarDatos()
}

class TvViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TvViewModel::class.java)) {
            return TvViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}