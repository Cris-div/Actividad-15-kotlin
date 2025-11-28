package com.miempresa.miscursoscrud_firebase

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object Destinations {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
}

@Composable
fun AuthApp() {
    // Crea y recuerda el NavController, que gestiona el back stack
    val navController = rememberNavController()

    // Llama al grafo de navegación principal
    AuthNavGraph(navController)
}


@Composable
fun AuthNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Destinations.LOGIN // La primera pantalla que se muestra es Login
    ) {

        // ------------------ Pantalla de LOGIN ------------------
        composable(Destinations.LOGIN) {
            LoginScreen(
                onNavigateToRegister = {
                    // Navega a la pantalla de registro
                    navController.navigate(Destinations.REGISTER)
                },
                onLoginSuccess = {
                    // Navega a HOME y limpia la pila de navegación:
                    // El "popUpTo" con "inclusive = true" asegura que el usuario no pueda volver al Login
                    // o Register usando el botón de atrás del sistema.
                    navController.navigate(Destinations.HOME) {
                        popUpTo(Destinations.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // ------------------ Pantalla de REGISTRO ------------------
        composable(Destinations.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    // Después de un registro exitoso, navega a HOME y limpia la pila de navegación.
                    navController.navigate(Destinations.HOME) {
                        popUpTo(Destinations.LOGIN) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    // Vuelve a la pantalla anterior (Login)
                    navController.popBackStack()
                }
            )
        }

        // ------------------ Pantalla HOME ------------------
        composable(Destinations.HOME) {
            HomeScreen(
                onLogout = {
                    // Al cerrar sesión, navega a LOGIN y limpia la pila de navegación.
                    navController.navigate(Destinations.LOGIN) {
                        popUpTo(Destinations.HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}