package mx.utng.smarthealthmonitor.tv

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mx.utng.shared.data.remote.NeonClient
import mx.utng.shared.data.remote.NeonRequest
import mx.utng.shared.data.remote.LecturaFcDto

class TvNeonRepository {
    /** Obtener historial completo de los 3 dispositivos */
    suspend fun obtenerHistorialCompleto(limite: Int = 50): List<LecturaFcDto> =
        withContext(Dispatchers.IO) {
            NeonClient.api.executeQuery(
                auth = NeonClient.AUTH_HEADER,
                connStr = NeonClient.CONN_STRING,
                request = NeonRequest(
                    query = """SELECT id,bpm,estado,dispositivo,hora,created_at
                    FROM lecturas_fc
                    ORDER BY created_at DESC
                    LIMIT $1""".trimIndent(),
                    params = listOf(limite)
                )
            ).rows
        }

    /** Estadísticas por dispositivo */
    suspend fun obtenerEstadisticas(): List<LecturaFcDto> =
        withContext(Dispatchers.IO) {
            NeonClient.api.executeQuery(
                auth = NeonClient.AUTH_HEADER,
                connStr = NeonClient.CONN_STRING,
                request = NeonRequest(
                    query = """SELECT dispositivo,
                    ROUND(AVG(bpm)) AS bpm,
                    'Promedio' AS estado,
                    MAX(hora) AS hora
                    FROM lecturas_fc
                    GROUP BY dispositivo""".trimIndent()
                )
            ).rows
        }

    /** Obtener alertas de las últimas 24 horas (Reto Extra) */
    suspend fun obtenerAlertas24Horas(): List<LecturaFcDto> =
        withContext(Dispatchers.IO) {
            NeonClient.api.executeQuery(
                auth = NeonClient.AUTH_HEADER,
                connStr = NeonClient.CONN_STRING,
                request = NeonRequest(
                    query = """SELECT id, bpm, estado, dispositivo, hora, created_at
                    FROM lecturas_fc
                    WHERE (bpm < 60 OR bpm > 100)
                      AND created_at > NOW() - INTERVAL '24 hours'
                    ORDER BY created_at DESC""".trimIndent()
                )
            ).rows
        }
}
