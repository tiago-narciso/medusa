package fr.uge.net.medusa.games.Narmin

import android.os.SystemClock
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlin.collections.plus
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds


class MotionControllerViewModel : ViewModel() {

}
    // =====================================================
    // Move the balls
    // =====================================================

    /**
     * Move the balls in a falling motion
     * Updates the positions of a list of falling balls for one frame
     * of animation
     */
    fun moveBalls(
        fallingBalls: List<FallingBall>, screenHeight: Float, screenWidth: Float
    ): List<FallingBall> {

        return fallingBalls.map { ball ->
            // y increases -> ball is falling
            val newY = ball.centerY + ball.speed
            // if ball left screen
            if (newY > 1000f) {
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
            modifier = Modifier.fillMaxSize()
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
            modifier = Modifier.fillMaxSize()
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



    fun isBallTouchingCrosshair(
        ball: FallingBall,
        crosshair: Crosshair,
        cameraOffsetX: Float,
        cameraOffsetY: Float
    ): Boolean{
        // Actual displayed position of the ball
        val displayedBallX = ball.centerX + cameraOffsetX
        val displayedBallY = ball.centerY + cameraOffsetY
        // Distance between their centers X axis
        val distanceX = displayedBallX - crosshair.centerX
        // Distance between their centers Y axis
        val distanceY = displayedBallY - displayedBallY
        // distance between their centers
        val distanceCenters = sqrt( distanceX* distanceX + distanceY*distanceY)
        // difference between their radii
        val distanceRadii = abs(ball.radius - crosshair.radius)
        // Sum of their radii
        val sumRadii = ball.radius + crosshair.radius
        return distanceCenters in distanceRadii..sumRadii

    }






    // =====================================================
    //  Main game logic
    // =====================================================
    /**
     * Main game logic
     */
    @Composable
    fun MotionControlledShooting(
        modifier: Modifier = Modifier,

        ) {
        val context = LocalContext.current
        var fallingBalls by remember {
            mutableStateOf<List<FallingBall>>(emptyList())
        }
        var cameraOffsetX by remember { mutableFloatStateOf(0f) }
        var cameraOffsetY by remember { mutableFloatStateOf(0f) }
        var score by remember { mutableIntStateOf(0 )}
        // Player has to catch 10 balls within 30 seconds
        val maxScore = 10
        val gameDuration = 30.seconds
        var startTime by remember { mutableLongStateOf(-1L) }


        val sensorController = remember { SensorController(context) }
        DisposableEffect(Unit) {
            sensorController.startListening()
            // clean up
            onDispose {
                sensorController.stopListening()
            }
        }


        BoxWithConstraints(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White),
        ) {
            val maxWidth = this.maxWidth.value
            val maxHeight = this.maxHeight.value
            val crosshair = Crosshair(
                centerX = maxWidth / 2f,
                centerY = maxHeight / 2f,
                radius = 60f,
                lineLength = 80f,
                color = Color.Black,
                strokeWidth = 5f
            )

            // create initial balls,
            // this couroutine is run once
            LaunchedEffect(Unit) {
                repeat(3) {
                    val ball = FallingBall.createBall(maxHeight, maxWidth)
                    fallingBalls = fallingBalls + ball
                }
            }
            // game loop: balls fall down
            LaunchedEffect(Unit) {
                while (true) {
                    val time = SystemClock.elapsedRealtime() - startTime
                    if(time >= gameDuration.inWholeMilliseconds){
                        if(score == maxScore){
                                ; // won
                        }else{
                            // lost
                        }
                    }
                    Text(" you reached a 100 clicks after $time")
                    val targetX = -sensorController.tiltX * 80f
                    val targetY = -sensorController.tiltY * 80f
                    cameraOffsetX += (targetX - cameraOffsetX) * 0.1f
                    cameraOffsetY += (targetY - cameraOffsetY) * 0.1f
                    cameraOffsetX = cameraOffsetX.coerceIn(-200f, 200f)

                    cameraOffsetY = cameraOffsetY.coerceIn(-200f, 200f)
                    Log.i("MotionController", "titltx ${sensorController.tiltX}")
                    Log.i("MotionController", "titlty ${sensorController.tiltX}")
                    Log.i("MotionController", "cameraOffsetX ${cameraOffsetX}")
                    Log.i("MotionController", "cameraOffsetY ${cameraOffsetY}")
                    fallingBalls = moveBalls(fallingBalls, maxHeight, maxWidth)
                    fallingBalls.forEach { b ->
                            if (isBallTouchingCrosshair(
                                    b,
                                    crosshair,
                                    cameraOffsetX,
                                    cameraOffsetY
                                )
                            ) {
                                score++
                                if(score == maxScore){
                                    ; // won
                                }
                            }

                    }
                    delay(16)
                }
            }
            // draw the balls and the crosshair
            DrawBalls(
                fallingBalls,
                cameraOffsetX,
                cameraOffsetY
            )
            DrawCrosshair(crosshair)
        }

    }






