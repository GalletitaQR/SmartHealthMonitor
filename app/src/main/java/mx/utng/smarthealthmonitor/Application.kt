package mx.utng.smarthealthmonitor

import android.app.Application
import mx.utng.shared.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.data.SimpleHealthServer
import mx.utng.smarthealthmonitor.data.sync.NeonSyncWorker

class SmartHealthApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SmartHealthRepository.init(this)
        SimpleHealthServer().start(fi.iki.elonen.NanoHTTPD.SOCKET_READ_TIMEOUT, false)
        
        // Programar sync periódico con Neon
        NeonSyncWorker.schedule(this)
    }
}