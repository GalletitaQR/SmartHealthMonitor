package mx.utng.smarthealthmonitor.data

import fi.iki.elonen.NanoHTTPD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mx.utng.shared.data.SmartHealthRepository

class SimpleHealthServer : NanoHTTPD(8888) {

    override fun serve(session: IHTTPSession): Response {
        return when (session.method) {
            Method.POST -> {
                session.parseBody(HashMap())
                val params = session.parameters
                val fc = params["fc"]?.firstOrNull()?.toIntOrNull()
                if (fc != null) {
                    runBlocking { SmartHealthRepository.actualizarFC(fc) }
                    newFixedLengthResponse(Response.Status.OK, "text/plain", "OK")
                } else {
                    newFixedLengthResponse(Response.Status.BAD_REQUEST, "text/plain", "falta fc")
                }
            }
            else -> {
                val fcActual = SmartHealthRepository.fcFlow.value
                val pasos = SmartHealthRepository.pasosFlow.value
                val json = """{"fc":$fcActual,"pasos":$pasos}"""
                newFixedLengthResponse(Response.Status.OK, "application/json", json)
            }
        }
    }
}