
package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.ui.DetalleLineaScreen
import com.example.myapplication.ui.DetalleProyectoScreen
import com.example.myapplication.ui.PantallaIndicadores
import com.example.myapplication.ui.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                onLineaClick = { idLinea ->
                    navController.navigate(Screen.DetalleLinea.createRoute(idLinea))
                },
                onProyectoClick = { idProyecto ->
                    navController.navigate(Screen.DetalleProyecto.createRoute(idProyecto))
                },
                onIndicadoresClick = {
                    navController.navigate(Screen.Indicadores.route)
                }
            )
        }

        composable(
            route = Screen.DetalleLinea.route,
            arguments = listOf(navArgument("idLinea") { type = NavType.IntType })
        ) { backStackEntry ->
            val idLinea = backStackEntry.arguments?.getInt("idLinea") ?: 0
            DetalleLineaScreen(
                idLinea = idLinea,
                onProyectoClick = { idProyecto ->
                    navController.navigate(Screen.DetalleProyecto.createRoute(idProyecto))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.DetalleProyecto.route,
            arguments = listOf(navArgument("idProyecto") { type = NavType.IntType })
        ) { backStackEntry ->
            val idProyecto = backStackEntry.arguments?.getInt("idProyecto") ?: 0
            DetalleProyectoScreen(
                idProyecto = idProyecto,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Screen.Indicadores.route) {
            PantallaIndicadores(onBackClick = { navController.popBackStack() })
        }
    }
}
 