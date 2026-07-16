package mx.utng.smarthealthmonitor.tv

import mx.utng.shared.data.db.LecturaFC

data class TvUiState(
    val fcActual: Int = 0,
    val fcEstado: String = "Normal",
    val ultimaHora: String = "",
    val isLoading: Boolean = true,
    val lecturas: List<LecturaFC> = emptyList(),
    val estadisticas: List<LecturaFC> = emptyList(),
    val error: String? = null
)