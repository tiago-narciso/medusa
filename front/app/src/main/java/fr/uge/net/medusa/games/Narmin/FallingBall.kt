package fr.uge.net.medusa.games.Narmin

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
                Color.Red,
                Color.Blue,
                Color.Green,
                Color.Magenta,
                Color.Cyan
            ).random(),
            radius = Random.nextFloat() * 40f + 30f,
            centerX = Random.nextFloat() * screenWidth,
            // spawn above screen
            centerY = Random.nextFloat() * -screenHeight,
            speed = Random.nextFloat() * 10f + 5f
        )
    }

}
}
