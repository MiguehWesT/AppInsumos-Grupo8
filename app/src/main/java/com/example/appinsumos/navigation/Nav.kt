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
import com.example.appinsumos.ui.screens.DetallePedidoScreen
import com.example.appinsumos.ui.screens.HomeScreen
import com.example.appinsumos.ui.screens.PerfilScreen
import com.example.appinsumos.ui.screens.SeguimientoPedidoScreen
import com.example.appinsumos.ui.screens.SolicitarInsumosScreen
import com.example.appinsumos.viewmodel.InsumosViewModel
import com.example.appinsumos.viewmodel.InsumosViewModelFactory
import com.example.appinsumos.viewmodel.PerfilViewModel
import com.example.appinsumos.viewmodel.PerfilViewModelFactory
import com.example.appinsumos.viewmodel.SeguimientoViewModel
import com.example.appinsumos.viewmodel.SeguimientoViewModelFactory

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
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(Screen.SolicitarInsumos.route) {
            val viewModel: InsumosViewModel = viewModel(
                factory = InsumosViewModelFactory(pedidoRepository)
            )
            SolicitarInsumosScreen(navController = navController, viewModel = viewModel)
        }

        composable(Screen.Seguimiento.route) {
            val viewModel: SeguimientoViewModel = viewModel(
                factory = SeguimientoViewModelFactory(pedidoRepository)
            )
            SeguimientoPedidoScreen(navController = navController, viewModel = viewModel)
        }

        composable(Screen.Perfil.route) {
            val viewModel: PerfilViewModel = viewModel(
                factory = PerfilViewModelFactory(usuarioRepository)
            )
            PerfilScreen(navController = navController, viewModel = viewModel)
        }

        composable(
            route = Screen.DetallePedido.route,
            arguments = listOf(navArgument("pedidoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val pedidoId = backStackEntry.arguments?.getString("pedidoId") ?: ""
            val viewModel: SeguimientoViewModel = viewModel(
                factory = SeguimientoViewModelFactory(pedidoRepository)
            )
            DetallePedidoScreen(
                pedidoId = pedidoId,
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}