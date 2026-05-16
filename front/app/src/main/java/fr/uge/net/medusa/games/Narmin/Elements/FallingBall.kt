package fr.uge.net.medusa.games.Narmin.Elements

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

data class FallingBall(
    val color: Color,
    val radius: Float,
    val centerX: Float,
    val centerY: Float,
    val speed: Float

){
    companion object {

    fun createBall(
        screenHeight: Float,
        screenWidth: Float): FallingBall {
        return FallingBall(
            color = listOf(
                Color.Companion.Red,
                Color.Companion.Blue,
                Color.Companion.Green,
                Color.Companion.Magenta,
                Color.Companion.Cyan
            ).random(),
            radius = Random.Default.nextFloat() * 40f + 30f,
            centerX = Random.Default.nextFloat() * screenWidth,
            // spawn above screen
            centerY = Random.Default.nextFloat() * -screenHeight,
            speed = Random.Default.nextFloat() * 10f + 5f
        )
    }

}
}