package fr.uge.net.medusa.games.Narmin.game

import android.os.SystemClock
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import fr.uge.net.medusa.games.Narmin.Elements.Crosshair
import fr.uge.net.medusa.games.Narmin.Elements.FallingBall
import fr.uge.net.medusa.games.Narmin.animation.Animate
import fr.uge.net.medusa.games.Narmin.animation.Draw
import fr.uge.net.medusa.games.Narmin.game.logic.Logic
import fr.uge.net.medusa.games.Narmin.sensor.SensorController
import fr.uge.net.medusa.games.Narmin.utils.Utils
import kotlinx.coroutines.delay
import kotlin.collections.plus
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds




    @Composable
    fun ShootingGame(modifier: Modifier = Modifier) {
        var gameStarted by remember { mutableStateOf(false) }
        var gameFinished by remember { mutableStateOf(false) }
        var score by remember { mutableIntStateOf(0) }
        var won by remember { mutableStateOf(false) }

        when {
            // can end modifier here
            !gameStarted -> {
                MemorySettingsScreen(
                    GameConfig.GAME_DURATION.inWholeSeconds,
                    GameConfig.MIN_SCORE,
                    onStartGame = {  -> gameStarted = true })
            }
            gameFinished -> {
                MemoryEndScreen(won, score, onResetGame = {
                    score = 0
                    gameStarted = false
                    gameFinished = false
                })
            }
            else -> {
                MotionControlledShootingManager(
                    modifier,
                    GameConfig.MIN_SCORE,
                    GameConfig.GAME_DURATION.inWholeMilliseconds,
                    startTime = SystemClock.elapsedRealtime(),
                    onEndGame = { nbBalls , wonGame->
                        score = nbBalls
                        gameFinished = true
                        won = wonGame
                    })

            }

        }
    }

    @Composable
    fun MemorySettingsScreen(
        gameDuration: Long,
        minNbPoints: Int,
        onStartGame: () -> Unit
    ) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(text = "Motion Shooting game",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text =
                "Shoot at least $minNbPoints balls\n" +
                        "during $gameDuration seconds",
                fontSize = 18.sp,
                textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = {
                    onStartGame()
                }
            ) {
                Text(text = "Start",
                    fontSize = 20.sp,
                )
            }
        }
    }

    @Composable
    fun MemoryEndScreen(
        won: Boolean,
        score: Int,
        onResetGame: () -> Unit) {
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
            text =
                if (won)
                    " YOU WON!"
                else
                    "GAME OVER HAHAHAHA",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color =
                if (won)
                    Color(0xFF2E7D32)
                else
                    Color(0xFFC62828)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Balls shot: $score",
                fontSize = 22.sp,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = { onResetGame() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Play again",
                    fontSize = 20.sp,
                )
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
    fun MotionControlledShootingManager(
        modifier: Modifier = Modifier,
        maxScore : Int,
        gameDuration: Long,
        startTime: Long,
        onEndGame: (Int, Boolean) -> Unit,
        ) {
        // Fields
        val context = LocalContext.current
        var fallingBalls by remember { mutableStateOf<List<FallingBall>>(emptyList()) }
        var cameraOffsetX by remember { mutableFloatStateOf(0f) }
        var cameraOffsetY by remember { mutableFloatStateOf(0f) }
        var score by remember { mutableIntStateOf(0 )}
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
                repeat(GameConfig.NB_BALLS) {
                    val ball = FallingBall.createBall(maxHeight, maxWidth)
                    fallingBalls = fallingBalls + ball
                }
            }
            // game loop: balls fall down
            LaunchedEffect(Unit) {
                while (true) {
                    val remainingSeconds = Utils.computeRemainingSeconds(
                            startTime,
                            gameDuration)
                    message = "Score: $score | Time: $remainingSeconds"
                    if(remainingSeconds == 0.toLong()){
                        var won = false
                        if(score == maxScore){
                               won = true
                        }else{
                               won = false
                        }
                        onEndGame(score, won)
                    }
                    val targetX = -sensorController.tiltX * GameConfig.CAMERA_SENSITIVITY
                    val targetY = -sensorController.tiltY * GameConfig.CAMERA_SENSITIVITY
                    cameraOffsetX = Animate.updateCameraOffset(
                        cameraOffsetX,
                        targetX,
                        GameConfig.SMOOTHING,
                        GameConfig.MAX_OFFSET
                    )
                    cameraOffsetY = Animate.updateCameraOffset(
                        cameraOffsetY,
                        targetY,
                        GameConfig.SMOOTHING,
                        GameConfig.MAX_OFFSET
                    )
                    // increase speed by 10 percent every yime the score increases
                    val speedMultiplier = 1f +(score * GameConfig.speedMultiplier)
                    fallingBalls = Animate.moveBalls(fallingBalls, maxHeight,
                        maxWidth, speedMultiplier)
                    fallingBalls =  Logic.processCollisions(
                        fallingBalls,
                        crosshair,
                        cameraOffsetX,
                        cameraOffsetY,
                        maxWidth,
                        onHit = { score++ }
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






