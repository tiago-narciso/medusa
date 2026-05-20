package fr.uge.net.medusa.models

import java.util.UUID

data class NearCard(
    val uniqueId: UUID,
    val lat: Double,
    val long: Double,
    val wikidataId: String
)
