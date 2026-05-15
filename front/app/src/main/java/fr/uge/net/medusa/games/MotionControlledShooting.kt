package fr.uge.net.medusa.games

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import kotlin.random.Random


class MotionControllerViewModel : ViewModel() {

}
// =====================================================
// Move the balls
// =====================================================

/**
 * Move the balls in a falling motion
 */
fun moveBalls(
    fallingBalls: List<FallingBall>,
    screenHeight: Float,
    screenWidth: Float
): List<FallingBall> {

    return fallingBalls.map { ball ->
        // y increases -> ball is falling
        val newY = ball.y + ball.speed
        // if ball left screen
        if (newY > 1000f) {
            // put ball above the screen so it falls again
            ball.copy(
                y = Random.nextFloat() * -1000f,
                x = Random.nextFloat() * screenWidth // random so each ball falls in a different pos
            )
        } else {
            // continue falling
            ball.copy(y = newY)
        }
    }
}

// =====================================================
// Draw the balls and the crosshair
// =====================================================
/**
 * Draw the balls and crosshair using canvas
 */
@Composable
fun DrawBallsCrosshair(fallingBalls: List<FallingBall>,
                       cameraOffsetX:Float,
                       cameraOffsetY:Float, ){
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        // display the shooters
        fallingBalls.forEach { ball ->
            drawCircle(
                color = ball.color,
                radius = ball.radius,
                center = Offset(
                    ball.x + cameraOffsetX,
                    ball.y + cameraOffsetY
                )
            )
        }
        // display the crosshair
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        drawCircle(
            color = Color.Black,
            radius = 60f,
            center = Offset(centerX, centerY),
            style = Stroke(width = 5f)
        )

        drawLine(
            color = Color.Black,
            start = Offset(centerX - 80f, centerY),
            end = Offset(centerX + 80f, centerY),
            strokeWidth = 5f
        )

        drawLine(
            color = Color.Black,
            start = Offset(centerX, centerY - 80f),
            end = Offset(centerX, centerY + 80f),
            strokeWidth = 5f
        )
    }
}



// =====================================================
//  Main game logic
// =====================================================
/**
 * Main game logic
 */
@Composable
@Preview
fun MotionControlledShooting(
    modifier: Modifier = Modifier,

    ) {
    val context = LocalContext.current
    var fallingBalls by remember {
        mutableStateOf<List<FallingBall>>(emptyList()) }
    var cameraOffsetX by remember { mutableFloatStateOf(0f) }
    var cameraOffsetY by remember { mutableFloatStateOf(0f) }
    val sensorController = remember {SensorController(context) }
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
                val targetX = -sensorController.tiltX * 80f
                val targetY = -sensorController.tiltY * 80f
                cameraOffsetX += (targetX - cameraOffsetX) * 0.1f
                cameraOffsetY += (targetY - cameraOffsetY) * 0.1f
                cameraOffsetX =
                    cameraOffsetX.coerceIn(-200f, 200f)

                cameraOffsetY =
                    cameraOffsetY.coerceIn(-200f, 200f)
                Log.i("MotionController", "titltx ${sensorController.tiltX}")
                Log.i("MotionController", "titlty ${sensorController.tiltX}")
                Log.i("MotionController", "cameraOffsetX ${cameraOffsetX}")
                Log.i("MotionController", "cameraOffsetY ${cameraOffsetY}")


                fallingBalls = moveBalls(fallingBalls, maxHeight, maxWidth)
                delay(16)
            }
        }
        // draw the balls and the crosshair
        DrawBallsCrosshair(fallingBalls,cameraOffsetX, cameraOffsetY )
    }




}




