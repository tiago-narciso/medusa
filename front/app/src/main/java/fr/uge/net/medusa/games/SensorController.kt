package fr.uge.net.medusa.games

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import kotlin.math.abs

class SensorController (context: Context): SensorEventListener {
    private val sensorManager  =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    // ----------Tilt values-------------//
    var tiltX: Float  = 0f
        private set // setter is private so value does not get changed
    var tiltY: Float  = 0f
        private set
    private var neutralX = 0f

    private var neutralY = 0f

    private var calibrated = false
    // ----------Start listening-------------//
    fun startListening() {
        sensorManager.registerListener(
            this,
            accelerometer,
            SensorManager.SENSOR_DELAY_GAME
        )
    }
    // ----------Stop listening-------------//

    fun stopListening(){
        sensorManager.unregisterListener(this)
    }

    // ----------Sensor Callback-------------//

    override fun onSensorChanged(
        event: SensorEvent
    ) {

        val rawX = event.values[0]

        val rawY = event.values[1]

        val rawZ = event.values[2]

        // =============================================
        // PRINT RAW SENSOR VALUES
        // =============================================

        Log.i(
            "MotionController",
            "rawX = $rawX"
        )

        Log.i(
            "MotionController",
            "rawY = $rawY"
        )

        Log.i(
            "MotionController",
            "rawZ = $rawZ"
        )

        // =============================================
        // CALIBRATE NEUTRAL POSITION ONCE
        // =============================================

        if (!calibrated) {

            neutralX = rawX

            neutralY = rawY

            calibrated = true
        }

        // =============================================
        // RELATIVE TILT
        // =============================================

        tiltX =
            if (abs(rawX - neutralX) < 0.3f)
                0f
            else
                rawX - neutralX

        tiltY =
            if (abs(rawY - neutralY) < 0.3f)
                0f
            else
                rawY - neutralY

        // =============================================
        // PRINT RELATIVE VALUES
        // =============================================

        Log.i(
            "MotionController",
            "tiltX = $tiltX"
        )

        Log.i(
            "MotionController",
            "tiltY = $tiltY"
        )
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        TODO("Not yet implemented")
    }




}