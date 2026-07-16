package mx.utng.shared.data.remote

import kotlinx.serialization.Serializable
import retrofit2.http.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import mx.utng.shared.BuildConfig

/** Request genérico para la Neon HTTP API */
@Serializable
data class NeonRequest(val query: String, val params: List<Any> = emptyList())

/** Response de la Neon HTTP API */
@Serializable
data class NeonResponse<T>(
    val rows: List<T> = emptyList(),
    val rowCount: Int = 0,
    val command: String = ""
)

/** DTO de lectura FC (mapea fila de PostgreSQL) */
@Serializable
data class LecturaFcDto(
    val id: Int = 0,
    val bpm: Int,
    val estado: String,
    val dispositivo: String,
    val hora: String,
    val fecha: String = "",
    val created_at: String = ""
)

fun LecturaFcDto.toLecturaFC() = mx.utng.shared.data.db.LecturaFC(
    id = id,
    bpm = bpm,
    estado = estado,
    dispositivo = dispositivo,
    hora = hora,
    sincronizado = true
)

/** Interfaz Retrofit para la Neon HTTP API */
interface NeonApiService {
    @POST("sql")
    suspend fun executeQuery(
        @Header("Authorization") auth: String,
        @Header("Neon-Connection-String") connStr: String,
        @Body request: NeonRequest
    ): NeonResponse<LecturaFcDto>
}

object NeonClient {
    private const val BASE_URL = "https://${BuildConfig.NEON_HOST}/"
    val AUTH_HEADER = "Bearer ${BuildConfig.NEON_API_KEY}"
    
    val CONN_STRING = if (BuildConfig.NEON_CONN_STRING.isNotEmpty() && !BuildConfig.NEON_CONN_STRING.contains("[usuario]")) {
        BuildConfig.NEON_CONN_STRING
    } else {
        "postgresql://neondb_owner:napi_placeholder@${BuildConfig.NEON_HOST}/neondb?sslmode=require"
    }

    val api: NeonApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }).build()
            )
            .build()
            .create(NeonApiService::class.java)
    }
}
