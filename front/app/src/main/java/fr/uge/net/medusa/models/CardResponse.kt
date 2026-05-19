package fr.uge.net.medusa.models

import java.util.UUID

data class CardResponse(
    val wikidataId: UUID,
    val uniqueId:UUID,
    val placeOfBirth: String,
    val power:Int
)
