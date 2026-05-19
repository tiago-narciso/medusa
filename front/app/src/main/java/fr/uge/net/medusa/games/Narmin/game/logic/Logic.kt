package fr.uge.net.medusa.games.Narmin.game.logic

import fr.uge.net.medusa.games.Narmin.Elements.Crosshair
import fr.uge.net.medusa.games.Narmin.Elements.FallingBall
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.random.Random

class Logic {

    companion object {
        fun isBallTouchingCrosshair(
            ball: FallingBall,
            crosshair: Crosshair,
            cameraOffsetX: Float,
            cameraOffsetY: Float
        ): Boolean {
            // Actual displayed position of the ball
            val displayedBallX = ball.centerX + cameraOffsetX
            val displayedBallY = ball.centerY + cameraOffsetY
            // Distance between their centers X axis
            val distanceX = displayedBallX - crosshair.centerX
            // Distance between their centers Y axis
            val distanceY = displayedBallY - crosshair.centerY
            // distance between their centers
            val distanceCenters = sqrt(distanceX * distanceX + distanceY * distanceY)
            // difference between their radii
            val distanceRadii = abs(ball.radius - crosshair.radius)
            // Sum of their radii
            val sumRadii = ball.radius + crosshair.radius
            return distanceCenters in distanceRadii..sumRadii

        }


        fun processCollisions(
            fallingBalls: List<FallingBall>,
            crosshair: Crosshair,
            cameraOffsetX: Float,
            cameraOffsetY: Float,
            maxWidth: Float,
            onHit: () -> Unit
        ): List<FallingBall> {
            return fallingBalls.map { b ->
                if (isBallTouchingCrosshair(
                        b,
                        crosshair,
                        cameraOffsetX,
                        cameraOffsetY
                    )
                ) {
                    onHit()
                    // hide B so it disappears from the frame
                    b.copy(
                        centerX = Random.nextFloat() * maxWidth,
                        centerY = -1000f
                    )
                } else {
                    b
                }

            }
        }
    }
}
