package mx.utng.smarthealthmonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import mx.utng.smarthealthmonitor.navigation.SmartHealthNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // NavGraph es ahora el punto de entrada — no LoginScreen directamente
            SmartHealthNavGraph()
        }
    }

}

