package com.example.appinsumos.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.appinsumos.data.PedidoRepository
import com.example.appinsumos.data.UsuarioRepository
import com.example.appinsumos.ui.screens.*
import com.example.appinsumos.viewmodel.*

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
fun NavGraph(
    navController: NavHostController,
    pedidoRepository: PedidoRepository,
    usuarioRepository: UsuarioRepository
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        // 🏠 Pantalla principal
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        // 📦 Solicitar insumos
        composable(Screen.SolicitarInsumos.route) {
            val viewModel: InsumosViewModel = viewModel(
                factory = InsumosViewModelFactory(pedidoRepository)
            )
            SolicitarInsumosScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        // 🚚 Seguimiento de pedidos
        composable(Screen.Seguimiento.route) {
            val viewModel: SeguimientoViewModel = viewModel(
                factory = SeguimientoViewModelFactory(pedidoRepository)
            )
            SeguimientoPedidoScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        // 👤 Perfil de usuario (con cámara + GPS)
        composable(Screen.Perfil.route) {
            val perfilViewModel: PerfilViewModel = viewModel(
                factory = PerfilViewModelFactory(usuarioRepository)
            )
            PerfilScreen(
                navController = navController,
                viewModel = perfilViewModel
            )
        }

        // 📄 Detalle de pedido
        composable(
            route = Screen.DetallePedido.route,
            arguments = listOf(
                navArgument("pedidoId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val pedidoId = backStackEntry.arguments?.getString("pedidoId") ?: ""
            val seguimientoViewModel: SeguimientoViewModel = viewModel(
                factory = SeguimientoViewModelFactory(pedidoRepository)
            )
            DetallePedidoScreen(
                pedidoId = pedidoId,
                navController = navController,
                viewModel = seguimientoViewModel
            )
        }
    }
}
