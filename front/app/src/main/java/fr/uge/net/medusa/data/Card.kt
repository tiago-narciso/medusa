package fr.uge.net.medusa.data

import java.util.UUID

data class Card(
    val wikidataId: UUID,
    val uniqueId: UUID,
    val placeOfBirth: String,
    val power:Int,
    val acquisitionDate: String
)