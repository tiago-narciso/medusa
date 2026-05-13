package fr.uge.net.medusa.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import fr.uge.net.medusa.activities.LoginScreenActivity
import fr.uge.net.medusa.activities.RegisterScreenActivity
import kotlinx.coroutines.delay
import fr.uge.net.medusa.activities.LoginScreenActivity
import fr.uge.net.medusa.activities.RegisterScreenActivity

/**
 * Main navigation host of the application.
 *
 * Defines:
 * - all app screens/routes
 * - navigation flow between screens
 * - authentication flow
 * - startup/loading flow
 *
 * Navigation structure:
 * LOADING
 *    ↓
 * LOGIN
 *    ├── REGISTER
 *    └── MAIN_GAME
 *
 * Uses NavController to navigate between composable screens.
 */


@Composable
fun MedusaNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(navController = navController, startDestination = Routes.LOADING, modifier = modifier) {
        composable(Routes.LOADING) {
            LaunchedEffect(Unit) {
                delay(1000)
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.LOADING) { inclusive = true } // remove loading screen from back stack
                }
            }
            // todo: component with an effect that checks if the token is valid and retrieve user data before redirecting
            Text("Loading...")
        }
        composable(Routes.LOGIN) {
            LoginScreenActivity(
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) },
                onAuthenticated = {
                    navController.navigate(Routes.MAIN_GAME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreenActivity(
                onAuthenticated = {
                    navController.navigate(Routes.MAIN_GAME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.MAIN_GAME) {
            Text("User authenticated.") // todo main activity
        }
    }
}

