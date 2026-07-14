package mx.utng.wear.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController

object WearScreens {
    const val DASHBOARD = "wear_dashboard"
    const val ALERTA    = "wear_alerta"
    const val HISTORIAL = "wear_historial"
}

@Composable
fun SmartHealthWearNavGraph() {
    val navController = rememberSwipeDismissableNavController()

    // 👇 Una sola instancia, scopeada a la Activity (vive mientras dure la sesión)
    val sharedViewModel: WearDashboardViewModel = viewModel()

    SwipeDismissableNavHost(
        navController    = navController,
        startDestination = WearScreens.DASHBOARD
    ) {
        composable(WearScreens.DASHBOARD) {
            WearDashboardScreen(
                viewModel         = sharedViewModel,
                onAlertClick      = { navController.navigate(WearScreens.ALERTA) },
                onHistorialClick  = { navController.navigate(WearScreens.HISTORIAL) }
            )
        }
        composable(WearScreens.ALERTA) {
            val fc by sharedViewModel.fc.collectAsState()
            WearAlertaScreen(
                fc          = fc,
                onConfirmar = {
                    sharedViewModel.enviarAlerta(fc)
                    navController.popBackStack()
                },
                onCancelar  = { navController.popBackStack() }
            )
        }
        composable(WearScreens.HISTORIAL) {
            WearHistorialScreen(
                viewModel = sharedViewModel,
                onBack    = { navController.popBackStack() }
            )
        }
    }
}