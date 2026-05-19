package fr.uge.net.medusa.games.Narmin.game

import kotlin.time.Duration.Companion.seconds

object GameConfig {
    const val speedMultiplier = 0.1f
    const val MIN_SCORE = 10
    const val NB_BALLS = 3
    val GAME_DURATION= 30.seconds
    const val CAMERA_SENSITIVITY = 80f
    // how fast current camera moves toward target
    const val SMOOTHING = 0.1f
    // maximum distance the camera is allowed to move
    const val MAX_OFFSET = 200f



}