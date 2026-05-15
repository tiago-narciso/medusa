package fr.uge.net.medusa.games

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.remember

class SensorController (context: Context): SensorEventListener {
    private val sensorManager  =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    // ----------Tilt values-------------//
    var titltX: Float  = 0f
        private set // setter is private so value does not get changed
    var titltY: Float  = 0f
        private set
    // ----------Start listening-------------//
    fun startListening(){
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

    override fun onSensorChanged(event: SensorEvent) {

        titltX = event.values[0]
        titltX = event.values[1]

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        TODO("Not yet implemented")
    }




}