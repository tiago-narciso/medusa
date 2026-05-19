package fr.uge.net.medusa.games.Narmin.animation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import fr.uge.net.medusa.games.Narmin.Elements.Crosshair
import fr.uge.net.medusa.games.Narmin.Elements.FallingBall

class Draw {


    companion object {

        // =====================================================
        // Draw the balls and the crosshair
        // =====================================================
        /**
         * Draw the balls using canvas
         */

        @Composable
        fun DrawBalls(
            fallingBalls: List<FallingBall>,
            cameraOffsetX: Float,
            cameraOffsetY: Float,
        ) {
            Canvas(
                modifier = Modifier.Companion.fillMaxSize()
            ) {
                // display the balls
                fallingBalls.forEach { ball ->
                    drawCircle(
                        color = ball.color,
                        radius = ball.radius,
                        center = Offset(
                            ball.centerX + cameraOffsetX,
                            ball.centerY + cameraOffsetY
                        )
                    )
                }
            }
        }

        /**
         * Draw the crosshair using canvas
         */

        @Composable
        fun DrawCrosshair(
            crosshair: Crosshair
        ) {
            Canvas(
                modifier = Modifier.Companion.fillMaxSize()
            ) {

                // outer circle
                drawCircle(
                    color = crosshair.color,
                    radius = crosshair.radius,
                    center = Offset(
                        crosshair.centerX,
                        crosshair.centerY
                    ),
                    style = Stroke(
                        width = crosshair.strokeWidth
                    )
                )
                // horizontal line
                drawLine(
                    color = crosshair.color,
                    start = Offset(
                        crosshair.centerX - crosshair.lineLength,
                        crosshair.centerY
                    ),
                    end = Offset(
                        crosshair.centerX + crosshair.lineLength,
                        crosshair.centerY
                    ),
                    strokeWidth = crosshair.strokeWidth
                )
                // vertical line
                drawLine(
                    color = crosshair.color,
                    start = Offset(
                        crosshair.centerX,
                        crosshair.centerY - crosshair.lineLength
                    ),
                    end = Offset(
                        crosshair.centerX,
                        crosshair.centerY + crosshair.lineLength
                    ),
                    strokeWidth = crosshair.strokeWidth
                )
            }
        }
    }
}