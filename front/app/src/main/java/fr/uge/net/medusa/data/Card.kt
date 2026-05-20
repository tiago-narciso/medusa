package fr.uge.net.medusa.data

import java.util.UUID

data class Card(
    val personality:String,
    val wikidataId: String,
    val uniqueId: UUID,
    val placeOfBirth: String,
    val power:Int,
    val acquisitionDate: String
)