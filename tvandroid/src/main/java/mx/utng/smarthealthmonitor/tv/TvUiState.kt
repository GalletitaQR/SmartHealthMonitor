package mx.utng.smarthealthmonitor.tv

data class TvUiState(
    val fcActual: Int = 0,
    val fcEstado: String = "Normal",
    val ultimaHora: String = "",
    val isLoading: Boolean = true
)