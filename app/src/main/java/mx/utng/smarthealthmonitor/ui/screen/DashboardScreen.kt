package mx.utng.smarthealthmonitor.ui.screen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.utng.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.ui.components.FilaHistorial
import mx.utng.smarthealthmonitor.ui.components.TarjetaDato
import mx.utng.smarthealthmonitor.ui.theme.SmartHealthMonitorTheme
import mx.utng.smarthealthmonitor.ui.viewmodel.DashboardViewModel
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onHistorialClick: () -> Unit = {},
    viewModel: DashboardViewModel = viewModel()
) {
    val fc by viewModel.fc.collectAsState()
    val historial by viewModel.historial.collectAsState()
    val pasos by viewModel.pasos.collectAsState()
    var mostrarAlerta by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Diálogo de alerta
    if (mostrarAlerta) {
        AlertaScreen(
            fc = fc,
            onDismiss = { mostrarAlerta = false },
            onConfirmar = {
                mostrarAlerta = false
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "✓ Alerta enviada a tus contactos",
                        duration = SnackbarDuration.Long
                    )
                }
            }
        )
    }

    SmartHealthMonitorTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },  // ← necesario
            topBar = {
                TopAppBar(
                    title = { Text("SmartHealth", style = MaterialTheme.typography.titleLarge) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { mostrarAlerta = true },   // ← abre el diálogo
                    containerColor = MaterialTheme.colorScheme.error
                ) {
                    Icon(Icons.Default.Warning, contentDescription = "Alerta")
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    TarjetaDato(
                        valor = "$fc",
                        unidad = "bpm",
                        label = "Frecuencia cardíaca",
                        colorValor = MaterialTheme.colorScheme.error
                    )
                }
                item {
                    TarjetaDato(
                        valor = "$pasos",
                        unidad = "pasos",
                        label = "Pasos hoy",
                        colorValor = MaterialTheme.colorScheme.primary
                    )
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Historial reciente", style = MaterialTheme.typography.titleMedium)
                        TextButton(onClick = onHistorialClick) { Text("Ver todo") }
                    }
                }
                items(historial, key = { it.id }) { lectura ->
                    FilaHistorial(lectura = lectura)
                }
                item {
                    OutlinedButton(
                        onClick = {
                            scope.launch { SmartHealthRepository.actualizarFC((60..110).random()) }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Simular dato del wearable (DEBUG)")
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    name = "Dashboard - Light",
    showSystemUi = true,
    device = "id:pixel_6"
)
@Preview(
    showBackground = true,
    name = "Dashboard - Dark",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun DashboardScreenPreview() {
    SmartHealthMonitorTheme {
        DashboardScreen()
    }
}