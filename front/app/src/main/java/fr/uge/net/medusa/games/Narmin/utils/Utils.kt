package fr.uge.net.medusa.games.Narmin.utils

import android.content.Context
import android.content.res.Resources
import android.os.SystemClock
import android.util.DisplayMetrics

class Utils {



    companion object {

        //Copied from https://gist.github.com/saeedsh92/1fccf4c28ac96c4180465f9cda5b69d1
        fun convertDpToPixel(dp: Float, context: Context?): Float {
            return if (context != null) {
                val resources = context.resources
                val metrics = resources.displayMetrics
                dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
            } else {
                val metrics = Resources.getSystem().displayMetrics
                dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
            }
        }

        fun convertMillisecondsToSeconds(time:Long):Long{
            return time/1000
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
    }
}