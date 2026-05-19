package fr.uge.net.medusa.activities

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import fr.uge.net.medusa.ui.components.navigation.BottomNavigationBar

/**
 * Full-screen tab with centered title and bottom navigation, shared by Profile, Ranking, Settings.
 */
@Composable
fun TabScreenScaffold(
    title: String,
    currentRoute: String,
    onNavigate: (String) -> Unit,
    innerPadding: PaddingValues,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = title)
        }
        BottomNavigationBar(
            currentRoute = currentRoute,
            onNavigate = onNavigate,
            modifier = Modifier.align(Alignment.BottomCenter),
            innerPadding = innerPadding,
        )
    }
}
