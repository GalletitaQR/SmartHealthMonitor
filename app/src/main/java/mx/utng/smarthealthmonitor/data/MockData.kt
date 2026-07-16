package mx.utng.smarthealthmonitor.data

import mx.utng.shared.data.db.LecturaFC

object MockData {
    val historialFC = listOf(
        LecturaFC(id = 10, bpm = 72, estado = "Normal", hora = "08:00"),
        LecturaFC(id = 11, bpm = 95, estado = "Normal", hora = "09:15"),
        LecturaFC(id = 12, bpm = 110, estado = "FC Alta", hora = "10:30"), // fuera de rango -> card roja
        LecturaFC(id = 13, bpm = 68, estado = "Normal", hora = "12:00"),
        LecturaFC(id = 14, bpm = 105, estado = "FC Alta", hora = "14:45"), // fuera de rango -> card roja
        LecturaFC(id = 15, bpm = 78, estado = "Normal", hora = "17:20")
    )
}