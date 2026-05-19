package fr.uge.net.medusa.games.Narmin.animation

import fr.uge.net.medusa.games.Narmin.Elements.FallingBall
import kotlin.random.Random

class Animate {

    companion object {
        // =====================================================
        // Move the balls
        // =====================================================

        /**
         * Move the balls in a falling motion
         * Updates the positions of a list of falling balls for one frame
         * of animation
         */
        fun moveBalls(
            fallingBalls: List<FallingBall>,
            screenHeight: Float,
            screenWidth: Float,
            speedMultiplier: Float
        ): List<FallingBall> {

            return fallingBalls.map { ball ->
                // y increases -> ball is falling
                val newY = ball.centerY +  (ball.speed * speedMultiplier)
                // if ball left screen
                if (newY > screenHeight) {
                    // put ball above the screen so it falls again
                    ball.copy(
                        centerY = Random.nextFloat() * -1000f,
                        centerX = Random.nextFloat() * screenWidth // random so each ball falls in a different pos
                    )
                } else {
                    // continue falling
                    ball.copy(centerY = newY)
                }
            }
        }

        fun updateCameraOffset(
            currentOffset: Float,
            targetOffset: Float,
            smoothing: Float,
            maxOffset: Float
        ): Float {

            val updated =
                currentOffset +
                        (targetOffset - currentOffset) * smoothing

            return updated.coerceIn(
                -maxOffset,
                maxOffset
            )
        }    }




}

/*
val targetX = -sensorController.tiltX * 80f
val targetY = -sensorController.tiltY * 80f
cameraOffsetX += (targetX - cameraOffsetX) * 0.1f
cameraOffsetY += (targetY - cameraOffsetY) * 0.1f
cameraOffsetX = cameraOffsetX.coerceIn(-200f, 200f)
cameraOffsetY = cameraOffsetY.coerceIn(-200f, 200f)

 */