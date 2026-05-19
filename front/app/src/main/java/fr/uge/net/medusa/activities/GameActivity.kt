package fr.uge.net.medusa.activities

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import fr.uge.net.medusa.ui.components.BottomNavigationBar
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

private fun getTileSource(isDark: Boolean) : OnlineTileSourceBase {
    if (isDark)
        return XYTileSource(
            "CartoDark",
            0, 19, 256, ".png",
            arrayOf(
                "https://a.basemaps.cartocdn.com/dark_all/",
                "https://b.basemaps.cartocdn.com/dark_all/",
                "https://c.basemaps.cartocdn.com/dark_all/"
            ),
            "© OpenStreetMap contributors, © CARTO"
        )
    else
        return XYTileSource(
            "CartoLight",
            0, 19, 256, ".png",
            arrayOf(
                "https://a.basemaps.cartocdn.com/light_all/",
                "https://b.basemaps.cartocdn.com/light_all/",
                "https://c.basemaps.cartocdn.com/light_all/"
            ),
            "© OpenStreetMap contributors, © CARTO"
        )
}

@Composable
fun GameScreen(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    innerPadding: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        GameMapScreen()

        BottomNavigationBar(
            currentRoute = currentRoute,
            onNavigate = onNavigate,
            modifier = Modifier.align(Alignment.BottomCenter),
            innerPadding = innerPadding
        )
    }
}

@Composable
private fun GameMapScreen() {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val isDark = isSystemInDarkTheme()
    val mapView = remember {
        Configuration.getInstance().userAgentValue = context.packageName
        MapView(context).apply {
            setTileSource(getTileSource(isDark))
            controller.setZoom(16.0)
            controller.setCenter(GeoPoint(48.8566, 2.3522))
        }
    }

    DisposableEffect(lifecycleOwner, mapView) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_DESTROY -> mapView.onDetach()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onDetach()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize()
        )
    }
}
