package mx.utng.smarthealthmonitor.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mx.utng.smarthealthmonitor.ui.screen.DashboardScreen
// Asegúrate de importar tus pantallas correctamente:
// import mx.utng.smarthealthmonitor.ui.screen.LoginScreen
import mx.utng.smarthealthmonitor.ui.theme.SmartHealthMonitorTheme
import mx.utng.smarthealthmonitor.LoginScreen

@Composable
fun SmartHealthNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        // Si 'Screen' no está definido como objeto/enum global, asegúrate de crearlo (ver abajo)
        startDestination = Screen.Login.route
    ) {
        // ── Login ──────────────────────────────────────
        composable(Screen.Login.route) {
            // Nota: Asegúrate de que LoginScreen exista en tu paquete de ui.screen
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true // Eliminar Login del back stack
                        }
                    }
                }
            )
        }

        // ── Dashboard ──────────────────────────────────
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onHistorialClick = {
                    navController.navigate(Screen.Historial.route)
                },
                onAlertClick = {
                    navController.navigate(Screen.Alerta.route)
                }
            )
        }

        // ── Historial ──────────────────────────────────
        composable(Screen.Historial.route) {
            PantallaEnConstruccion(
                titulo = "Historial completo",
                onBack = { navController.popBackStack() }
            )
        }

        // ── Alerta ─────────────────────────────────────
        composable(Screen.Alerta.route) {
            PantallaEnConstruccion(
                titulo = "Enviar alerta",
                onBack = { navController.popBackStack() }
            )
        }
    }
}

// Pantalla temporal para destinos no implementados aún
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEnConstruccion(titulo: String, onBack: () -> Unit) {
    SmartHealthMonitorTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(titulo) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Regresar"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Próximamente: $titulo",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}