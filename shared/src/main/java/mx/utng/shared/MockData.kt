package mx.utng.shared

import mx.utng.shared.data.db.LecturaFC

object MockData {
    val historialFC = listOf(
        LecturaFC(id = 10, valorBpm = 72, hora = "08:00"),
        LecturaFC(id = 11, valorBpm = 95, hora = "09:15"),
        LecturaFC(id = 12, valorBpm = 110, hora = "10:30"), // fuera de rango -> card roja
        LecturaFC(id = 13, valorBpm = 68, hora = "12:00"),
        LecturaFC(id = 14, valorBpm = 105, hora = "14:45"), // fuera de rango -> card roja
        LecturaFC(id = 15, valorBpm = 78, hora = "17:20")
    )
}