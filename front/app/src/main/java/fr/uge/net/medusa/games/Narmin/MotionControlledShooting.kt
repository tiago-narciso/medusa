package fr.uge.net.medusa.games.Narmin

import android.content.Context
import android.content.res.Resources
import android.os.SystemClock
import android.util.DisplayMetrics
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import fr.uge.net.medusa.games.Narmin.Elements.Crosshair
import fr.uge.net.medusa.games.Narmin.Elements.FallingBall
import fr.uge.net.medusa.games.Narmin.animation.Animate
import fr.uge.net.medusa.games.Narmin.draw.Draw
import fr.uge.net.medusa.games.Narmin.sensor.SensorController
import fr.uge.net.medusa.games.Narmin.utils.Utils
import kotlinx.coroutines.delay
import kotlin.collections.plus
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds


class MotionControllerViewModel : ViewModel() {

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
        val distanceY = displayedBallY - crosshair.centerY
        // distance between their centers
        val distanceCenters = sqrt( distanceX* distanceX + distanceY*distanceY)
        // difference between their radii
        val distanceRadii = abs(ball.radius - crosshair.radius)
        // Sum of their radii
        val sumRadii = ball.radius + crosshair.radius
        return distanceCenters in distanceRadii..sumRadii

    }


    fun computeRemainingSeconds(
        startTime: Long,
        gameDurationMillis: Long
    ): Long {
        val elapsed =
            SystemClock.elapsedRealtime() - startTime

        return (
                gameDurationMillis - elapsed
                ) / 1000
    }


    fun processCollisions(
        fallingBalls: List<FallingBall>,
        crosshair: Crosshair,
        cameraOffsetX: Float,
        cameraOffsetY: Float,
        maxWidth: Float,
        onHit: () -> Unit
    ): List<FallingBall>{
        return fallingBalls.map { b ->
            if (isBallTouchingCrosshair(
                    b,
                    crosshair,
                    cameraOffsetX,
                    cameraOffsetY
                )) {
                onHit()
                // hide B so it disappears from the frame
                b.copy(
                    centerX = Random.nextFloat() * maxWidth,
                    centerY = -1000f
                )
            }else{
                b
            }

        }
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
        // Fields
        val context = LocalContext.current
        var fallingBalls by remember { mutableStateOf<List<FallingBall>>(emptyList()) }
        var cameraOffsetX by remember { mutableFloatStateOf(0f) }
        var cameraOffsetY by remember { mutableFloatStateOf(0f) }
        var score by remember { mutableIntStateOf(0 )}
        // Player has to catch 10 balls within 30 seconds
        val maxScore = 10
        val gameDuration = 30.seconds
        var startTime by remember { mutableLongStateOf(SystemClock.elapsedRealtime()) }
        var message by remember { mutableStateOf("") }
        val sensorController = remember { SensorController(context) }

        //Start sensor
        DisposableEffect(Unit) {
            sensorController.startListening()
            // clean up
            onDispose {
                sensorController.stopListening()
            }
        }

        // Game
        BoxWithConstraints(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White),
        ) {
            val maxWidth = Utils.convertDpToPixel(this.maxWidth.value,context)
            val maxHeight = Utils.convertDpToPixel(this.maxHeight.value,context)
            val crosshair = Crosshair.createCrosshair(maxWidth, maxHeight)

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
                    val remainingSeconds =
                        computeRemainingSeconds(
                            startTime,
                            gameDuration.inWholeMilliseconds
                        )
                    message = "Score: $score | Time: $remainingSeconds"
                    if(remainingSeconds <= 0){
                        if(score == maxScore){
                                ; // won
                        }else{
                            // lost
                        }
                    }
                    val targetX = -sensorController.tiltX * 80f
                    val targetY = -sensorController.tiltY * 80f
                    cameraOffsetX = Animate.updateCameraOffset(
                        cameraOffsetX,
                        targetX,
                        0.1f,
                        200f
                    )
                    cameraOffsetY = Animate.updateCameraOffset(
                        cameraOffsetY,
                        targetY,
                        0.1f,
                        200f
                    )
                    fallingBalls = Animate.moveBalls(fallingBalls, maxHeight, maxWidth)
                    fallingBalls =  processCollisions(
                        fallingBalls,
                        crosshair,
                        cameraOffsetX,
                        cameraOffsetY,
                        maxWidth,
                        onHit = {
                            score++
                            if (score == maxScore) {
                                // won
                            }
                        }
                    )
                    delay(16)
                }
            }
            Text(
                text = message
            )
            // draw the balls and the crosshair
            Draw.DrawBalls(
                fallingBalls,
                cameraOffsetX,
                cameraOffsetY
            )
            Draw.DrawCrosshair(crosshair)
        }

    }






