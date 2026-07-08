package mx.utng.smarthealthmonitor
import android.app.Application
import mx.utng.shared.data.SmartHealthRepository

class SmartHealthApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SmartHealthRepository.init(this) // inicializar Room
    }
}