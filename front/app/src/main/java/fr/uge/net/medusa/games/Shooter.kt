package fr.uge.net.medusa.games

import android.content.Context

data class Shooter(
    val filename: String,
    val shooterName: String
){

    companion object{
        fun loadShooters(context: Context): List<Shooter>{
            val shooters = mutableListOf<Shooter>()
            val assetManager = context.assets
            val files = assetManager.list("fishes")
            if (files != null) {
                for (fileName in files) {
                    val fishName = fileName.removeSuffix(".jpeg")
                    val shooter = Shooter(fileName, fishName )
                    shooters.add(shooter)
                }
            }
            return shooters
        }


    }



}