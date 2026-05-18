package fr.uge.net.medusa.activities

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable

@Composable
fun SettingsActivity(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    innerPadding: PaddingValues,
) {
    TabScreenScaffold(
        title = "Settings",
        currentRoute = currentRoute,
        onNavigate = onNavigate,
        innerPadding = innerPadding,
    )
}
