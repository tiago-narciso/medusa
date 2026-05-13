package fr.uge.net.medusa.games

import android.content.Context

data class Shooters(
    val filename: String,
    val fishName: String
){

    companion object{
        fun loadShooters(context: Context): List<Shooters>{
            val fishes = mutableListOf<Shooters>()
            val assetManager = context.assets
            val files = assetManager.list("fishes")
            if (files != null) {
                for (fileName in files) {
                    val fishName = fileName.removeSuffix(".jpeg")
                    val fish = Shooters(fileName, fishName )
                    fishes.add(fish)
                }
            }
            return fishes
        }


    }



}