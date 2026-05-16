package fr.uge.net.medusa.games.Narmin

import androidx.compose.ui.graphics.Color

data class Crosshair(
    val radius: Float,
    val centerX: Float,
    val centerY: Float,
    val lineLength : Float,
    val color: Color,
    val strokeWidth: Float
)
