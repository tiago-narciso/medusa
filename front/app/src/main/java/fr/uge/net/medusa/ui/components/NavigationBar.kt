package fr.uge.net.medusa.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.uge.net.medusa.R
import fr.uge.net.medusa.navigation.Routes

private data class BottomTab(
    val route: String,
    val label: String,
    val icon: Int,
)

private val bottomTabs = listOf(
    BottomTab(Routes.MAIN_GAME, "Map", R.drawable.map),
    BottomTab(Routes.PROFILE, "Profile", R.drawable.profile),
    BottomTab(Routes.RANKING, "Ranking", R.drawable.ranking),
    BottomTab(Routes.SETTINGS, "Settings", R.drawable.settings),
)


@Composable
public fun BottomNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(84.dp + innerPadding.calculateBottomPadding()),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            bottomTabs.forEach { tab ->
                val selected = tab.route == currentRoute
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            if (!selected) {
                                onNavigate(tab.route)
                            }
                        }
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .fillMaxWidth(0.7f)
                            .background(if (selected) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f) else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(tab.icon),
                            modifier = Modifier
                                .size(27.dp)
                                .padding(vertical = 5.dp),
                            contentDescription = "${tab.label} logo",
                            tint = if (selected) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = tab.label,
                        color = if (selected) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewBottomNavigationBar() {
    BottomNavigationBar(currentRoute = Routes.MAIN_GAME, onNavigate = {}, innerPadding = PaddingValues())
}
