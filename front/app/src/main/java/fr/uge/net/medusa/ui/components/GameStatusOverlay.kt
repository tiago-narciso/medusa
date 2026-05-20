package fr.uge.net.medusa.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.uge.net.medusa.R
import fr.uge.net.medusa.activities.GameViewModel

@Composable
fun GameStatusOverlay(
    status: GameViewModel.GameStatus,
    onRequestLocationPermission: () -> Unit,
    onOpenLocationSettings: () -> Unit,
    onRetry: () -> Unit,
    hideLoadingState: Boolean = false,
    modifier: Modifier = Modifier
) {
    val overlayContent = when (status) {
        GameViewModel.GameStatus.Ready -> null
        GameViewModel.GameStatus.Loading -> if (hideLoadingState) {
            null
        } else {
            OverlayContent(
                title = stringResource(R.string.game_status_loading_title),
                message = stringResource(R.string.game_status_loading_message),
                primaryButtonText = stringResource(R.string.game_status_retry),
                onPrimaryAction = onRetry
            )
        }

        GameViewModel.GameStatus.NoInternet -> OverlayContent(
            title = stringResource(R.string.game_status_no_internet_title),
            message = stringResource(R.string.game_status_no_internet_message),
            primaryButtonText = stringResource(R.string.game_status_retry),
            onPrimaryAction = onRetry
        )

        GameViewModel.GameStatus.PermissionRequired -> OverlayContent(
            title = stringResource(R.string.game_status_permission_title),
            message = stringResource(R.string.game_status_permission_message),
            primaryButtonText = stringResource(R.string.game_status_grant_permission),
            onPrimaryAction = onRequestLocationPermission
        )

        GameViewModel.GameStatus.LocationDisabled -> OverlayContent(
            title = stringResource(R.string.game_status_location_disabled_title),
            message = stringResource(R.string.game_status_location_disabled_message),
            primaryButtonText = stringResource(R.string.game_status_open_settings),
            onPrimaryAction = onOpenLocationSettings
        )

        GameViewModel.GameStatus.NoPosition -> OverlayContent(
            title = stringResource(R.string.game_status_no_position_title),
            message = stringResource(R.string.game_status_no_position_message),
            primaryButtonText = stringResource(R.string.game_status_retry),
            onPrimaryAction = onRetry
        )
    }

    if (overlayContent == null) {
        return
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.padding(24.dp),
            tonalElevation = 8.dp,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = overlayContent.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = overlayContent.message,
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
                Button(
                    onClick = overlayContent.onPrimaryAction,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(overlayContent.primaryButtonText)
                }
            }
        }
    }
}

private data class OverlayContent(
    val title: String,
    val message: String,
    val primaryButtonText: String,
    val onPrimaryAction: () -> Unit
)
