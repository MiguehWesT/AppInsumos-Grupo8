package com.example.appinsumos.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.appinsumos.ui.*

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object SolicitarInsumos : Screen("solicitar_insumos")
    object Seguimiento : Screen("seguimiento")
    object Perfil : Screen("perfil")
    object DetallePedido : Screen("detalle_pedido/{pedidoId}") {
        fun createRoute(pedidoId: String) = "detalle_pedido/$pedidoId"
    }
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(Screen.SolicitarInsumos.route) {
            SolicitarInsumosScreen(navController = navController)
        }

        composable(Screen.Seguimiento.route) {
            SeguimientoPedidoScreen(navController = navController)
        }

        composable(Screen.Perfil.route) {
            PerfilScreen(navController = navController)
        }

        composable(
            route = Screen.DetallePedido.route,
            arguments = listOf(navArgument("pedidoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val pedidoId = backStackEntry.arguments?.getString("pedidoId") ?: ""
            DetallePedidoScreen(
                pedidoId = pedidoId,
                navController = navController
            )
        }
    }
}