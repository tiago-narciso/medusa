package fr.uge.net.medusa.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import fr.uge.net.medusa.activities.GameScreen
import fr.uge.net.medusa.activities.LoginScreenActivity
import fr.uge.net.medusa.activities.ProfileActivity
import fr.uge.net.medusa.activities.RankingActivity
import fr.uge.net.medusa.activities.RegisterScreenActivity
import fr.uge.net.medusa.activities.SettingsActivity
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
    innerPadding: PaddingValues
) {
    NavHost(navController = navController, startDestination = Routes.LOADING) {
        composable(Routes.LOADING) {
            LaunchedEffect(Unit) {
                delay(1000)
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.LOADING) { inclusive = true } // remove loading screen from back stack
                }
            }
            // todo: checks token valid, can access location etc ...
            // todo: component with an effect that checks if the token is valid and retrieve user data before redirecting
            Text(modifier = modifier.padding(innerPadding), text = "Loading...")
        }
        composable(Routes.LOGIN) {
            LoginScreenActivity(
                modifier = modifier.padding(innerPadding),
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
                modifier = modifier.padding(innerPadding),
                onAuthenticated = {
                    navController.navigate(Routes.MAIN_GAME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.MAIN_GAME) {
            GameScreen(
                currentRoute = Routes.MAIN_GAME,
                innerPadding = innerPadding,
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    }
                }
            )
        }
        composable(Routes.PROFILE) {
            ProfileActivity(
                currentRoute = Routes.PROFILE,
                innerPadding = innerPadding,
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    }
                }
            )
        }
        composable(Routes.RANKING) {
            RankingActivity(
                currentRoute = Routes.RANKING,
                innerPadding = innerPadding,
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    }
                },
            )
        }
        composable(Routes.SETTINGS) {
            SettingsActivity(
                currentRoute = Routes.SETTINGS,
                innerPadding = innerPadding,
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    }
                },
            )
        }
    }
}

