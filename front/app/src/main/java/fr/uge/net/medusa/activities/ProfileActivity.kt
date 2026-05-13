package fr.uge.net.medusa.activities

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable

@Composable
public fun ProfileActivity(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    innerPadding: PaddingValues,
) {
    TabScreenScaffold(
        title = "Profile",
        currentRoute = currentRoute,
        onNavigate = onNavigate,
        innerPadding = innerPadding,
    )
}