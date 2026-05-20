package fr.uge.net.medusa.activities

import android.Manifest
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.uge.net.medusa.R
import fr.uge.net.medusa.ui.components.GameStatusOverlay

@Composable
fun GameLoadingScreen(
    gameViewModel: GameViewModel,
    innerPadding: PaddingValues,
    onBootstrapComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val showBlockingMessage = gameViewModel.gameStatus in setOf(
        GameViewModel.GameStatus.NoInternet,
        GameViewModel.GameStatus.PermissionRequired,
        GameViewModel.GameStatus.LocationDisabled,
        GameViewModel.GameStatus.NoPosition
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        gameViewModel.forceRefresh(context)
    }

    LaunchedEffect(gameViewModel) {
        gameViewModel.runBootstrap(context)
    }

    LaunchedEffect(gameViewModel.isBootstrapped) {
        if (gameViewModel.isBootstrapped) {
            onBootstrapComplete()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        if (!showBlockingMessage) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Text(
                    text = stringResource(R.string.game_loading_startup),
                    modifier = Modifier.padding(top = 16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        GameStatusOverlay(
            status = gameViewModel.gameStatus,
            hideLoadingState = true,
            onRequestLocationPermission = {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            },
            onOpenLocationSettings = {
                context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            },
            onRetry = { gameViewModel.forceRefresh(context) }
        )
    }
}
