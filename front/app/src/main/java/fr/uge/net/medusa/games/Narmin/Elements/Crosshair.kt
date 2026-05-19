package fr.uge.net.medusa.games.Narmin.Elements

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

data class Crosshair(
    val radius: Float,
    val centerX: Float,
    val centerY: Float,
    val lineLength : Float,
    val color: Color,
    val strokeWidth: Float
) {
    companion object {
        fun createCrosshair(
            maxWidth:Float,
            maxHeight: Float): Crosshair {
            return  Crosshair(
                radius = 60f,
                centerX = maxWidth / 2f,
                centerY = maxHeight / 2f,
                lineLength = 80f,
                color = Color.Black,
                strokeWidth = 5f
            )
        }    }
}
