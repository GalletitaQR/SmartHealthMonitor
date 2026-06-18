package mx.utng.wear.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.wear.compose.material.*
import mx.utng.wear.presentation.theme.SmartHealthMonitorTheme

class WearMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState)
        setContent {
            SmartHealthWearNavGraph()
        }
    }
}
