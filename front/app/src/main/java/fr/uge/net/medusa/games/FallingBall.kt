package fr.uge.net.medusa.games

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

data class FallingBall(
    val color: Color,
    val radius: Float,
    val x: Float,
    val y: Float,
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
            x = Random.nextFloat() * screenWidth,
            // spawn above screen
            y = Random.nextFloat() * -screenHeight,
            speed = Random.nextFloat() * 10f + 5f
        )
    }

}
}
