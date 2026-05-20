package fr.uge.net.medusa.data

import java.util.UUID

data class cardItem(
    val personality:String,
    val wikidataId: UUID,
    val uniqueId: UUID,
    val placeOfBirth: String,
    val power:Int,
    val acquisitionDate: String
)