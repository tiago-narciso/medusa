package fr.uge.net.medusa.activities

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable

@Composable
fun RankingActivity(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    innerPadding: PaddingValues,
) {
    TabScreenScaffold(
        title = "Ranking",
        currentRoute = currentRoute,
        onNavigate = onNavigate,
        innerPadding = innerPadding,
    )
}
