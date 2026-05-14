package fr.uge.net.medusa.games

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlin.collections.plus
import kotlin.random.Random

class oldVersion {
    class MotionControllerViewModel : ViewModel() {

    }





























    fun createInitialShooters(shooters: List<Shooter>): FallingShooter {
        val randomShooter = shooters.random()
        return FallingShooter(
            shooter = randomShooter,
            x = Random.nextFloat() * 800f,
            y = Random.nextFloat() * -2000f,
            speed = Random.nextFloat() * 10f + 5f
        )
    }

    fun moveShooters(
        fallingShooters: List<FallingShooter>,
        screenHeight: Float,
        screenWidth: Float
    ): List<FallingShooter> {

        return fallingShooters.map { shooter ->
            // y increases -> shooter is falling
            val newY = shooter.y + shooter.speed
            // reset if below screen
            if (newY > screenHeight) {
                // put shooter above the screen so it falls again
                shooter.copy(
                    y = Random.nextFloat() * -1000f, // random so each shooter falls in a different pos
                    x = Random.nextFloat() * screenWidth
                )
            } else {
                // continue normal falling
                shooter.copy(y = newY)
            }
        }
    }

    @Composable
    @Preview
    fun MotionControlledShooting(
        modifier: Modifier = Modifier,

        ) {
        val shooters = Shooter.loadShooters(LocalContext.current)
        var fallingShooters by remember {
            mutableStateOf<List<FallingShooter>>(emptyList())
        }
        BoxWithConstraints(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White),
        ) {
            val maxWidth = this.maxWidth.value
            val maxHeight = this.maxHeight.value
            // create initial shooters,
            // this couroutine is run once
            LaunchedEffect(Unit) {
                repeat(10) {
                    val fallingShooter = createInitialShooters(shooters)
                    fallingShooters = fallingShooters + fallingShooter
                }
            }
            // game loop: shooters fall down
            LaunchedEffect(Unit) {
                while (true) {
                    fallingShooters = moveShooters(fallingShooters, maxHeight, maxWidth)
                    delay(16)
                }
            }
            // display the shooters
            fallingShooters.forEach { fallingShooter ->
                shooterDisplayer(
                    // position the meteors in different positions
                    shooter = fallingShooter.shooter, modifier = Modifier.offset {
                        IntOffset(
                            fallingShooter.x.toInt(), fallingShooter.y.toInt()
                        )
                    })
            }

        }

    }


    fun loadImageFromAssets(context: Context, path: String): ImageBitmap? {
        val assetManager = context.assets
        val istr = assetManager.open(path)
        val bitmap = BitmapFactory.decodeStream(istr)
        istr.close()
        return bitmap.asImageBitmap()
    }

    @Composable
    fun getAssetImage(path: String): ImageBitmap? {
        val context = LocalContext.current
        var bitmap by remember { mutableStateOf<ImageBitmap?>(null) } // we keep this bitmap in cache
        LaunchedEffect(path) {
            bitmap = loadImageFromAssets(context, path)
        }
        return bitmap
    }

    @Composable
    fun shooterDisplayer(shooter: Shooter, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
        Box(modifier) {
            val path = "fishes/" + shooter.filename
            getAssetImage(path)?.let { Image(it, "") }
            // in case it's null ?. only calls when result is non nullable

        }
    }




}