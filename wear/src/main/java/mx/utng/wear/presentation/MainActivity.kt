package mx.utng.wear.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var wearDataSender: WearDataSender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wearDataSender = WearDataSender(this)

        setContent {
            SimuladorFC(
                onEnviar = { bpm ->
                    lifecycleScope.launch {
                        wearDataSender.enviarFC(bpm)
                    }
                }
            )
        }
    }
}

@Composable
fun SimuladorFC(onEnviar: (Int) -> Unit) {
    var valorSimulado by remember { mutableStateOf(75) }
    var enviado by remember { mutableStateOf(false) }

    LaunchedEffect(valorSimulado) {
        enviado = false
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "FC Simulada",
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "$valorSimulado BPM",
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { if (valorSimulado > 30) valorSimulado-- },
                modifier = Modifier.size(42.dp)
            ) {
                Text("-", fontSize = 20.sp)
            }

            Button(
                onClick = { if (valorSimulado < 200) valorSimulado++ },
                modifier = Modifier.size(42.dp)
            ) {
                Text("+", fontSize = 20.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                onEnviar(valorSimulado)
                enviado = true
            }
        ) {
            Text(if (enviado) "✓ Enviado" else "Enviar", fontSize = 12.sp)
        }
    }
}