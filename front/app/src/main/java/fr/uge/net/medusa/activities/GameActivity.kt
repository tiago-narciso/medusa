package fr.uge.net.medusa.activities

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.provider.Settings
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.uge.net.medusa.R
import fr.uge.net.medusa.ui.components.GameStatusOverlay
import fr.uge.net.medusa.api.ApiProvider
import fr.uge.net.medusa.models.NearRequest
import fr.uge.net.medusa.ui.components.navigation.BottomNavigationBar
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon

private const val MAP_ZOOM_LEVEL = 18.0
private const val USER_ZONE_RADIUS_METERS = 150.0

private fun getTileSource(isDark: Boolean): OnlineTileSourceBase {
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
    innerPadding: PaddingValues,
    gameViewModel: GameViewModel = viewModel()
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        gameViewModel.forceRefresh(context)
    }

    LaunchedEffect(gameViewModel) {
        gameViewModel.startPositionLoop(context)
    }

    DisposableEffect(gameViewModel) {
        onDispose { gameViewModel.stopPositionLoop() }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        GameMapScreen(gameViewModel = gameViewModel)

        BottomNavigationBar(
            currentRoute = currentRoute,
            onNavigate = onNavigate,
            modifier = Modifier.align(Alignment.BottomCenter),
            innerPadding = innerPadding
        )

        GameStatusOverlay(
            status = gameViewModel.gameStatus,
            hideLoadingState = gameViewModel.isBootstrapped,
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

@Composable
private fun GameMapScreen(gameViewModel: GameViewModel) {
    val apiService = ApiProvider.getRealApi();
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val isDark = isSystemInDarkTheme()
    val mapView = remember {
        Configuration.getInstance().userAgentValue = context.packageName
        MapView(context).apply {
            setTileSource(getTileSource(isDark))
            setMultiTouchControls(false)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
            minZoomLevel = MAP_ZOOM_LEVEL
            maxZoomLevel = MAP_ZOOM_LEVEL
            controller.setZoom(MAP_ZOOM_LEVEL)
            controller.setCenter(gameViewModel.userPosition)
        }
    }

    val userZoneCircle = remember(mapView) {
        createUserZoneCircle(
            mapView = mapView,
            center = gameViewModel.userPosition
        ).also { circle ->
            mapView.overlays.add(circle)
        }
    }

    val userMarker = remember(mapView) {
        Marker(mapView).apply {
            icon = AppCompatResources.getDrawable(context, R.drawable.position)
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            position = gameViewModel.userPosition
        }.also { marker ->
            mapView.overlays.add(marker)
        }
    }

    var hasCenteredMap by remember { mutableStateOf(false) }
    LaunchedEffect(gameViewModel.userPosition) {
        userMarker.position = gameViewModel.userPosition
        userZoneCircle.points = Polygon.pointsAsCircle(
            gameViewModel.userPosition,
            USER_ZONE_RADIUS_METERS
        )
        if (!hasCenteredMap) {
            mapView.controller.setCenter(gameViewModel.userPosition)
            hasCenteredMap = true
        } else {
            mapView.controller.animateTo(gameViewModel.userPosition)
        }
        mapView.invalidate()
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
            modifier = Modifier.fillMaxSize(),
            update = { map ->
                // map.overlays.removeIf { it is Marker }
                gameViewModel.cards.forEach { card ->
                    val marker = Marker(map).apply {
                        position = GeoPoint(card.lat, card.long)
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        title = "Game Objective"

                        // Handle marker click events
                        setOnMarkerClickListener { clickedMarker, _ ->
                            clickedMarker.showInfoWindow()
                            true // return true to consume the click event
                        }
                    }
                    map.overlays.add(marker)
                }

                map.invalidate()
            }
        )
    }
}

private fun createUserZoneCircle(mapView: MapView, center: GeoPoint): Polygon {
    return Polygon(mapView).apply {
        fillPaint.color = Color.TRANSPARENT
        outlinePaint.color = Color.WHITE
        outlinePaint.strokeWidth = 4f
        outlinePaint.style = Paint.Style.STROKE
        outlinePaint.isAntiAlias = true
        outlinePaint.pathEffect = DashPathEffect(floatArrayOf(24f, 18f), 0f)
        points = Polygon.pointsAsCircle(center, USER_ZONE_RADIUS_METERS)
    }
}
