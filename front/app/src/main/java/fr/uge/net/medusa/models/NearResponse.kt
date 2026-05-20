package fr.uge.net.medusa.models

import fr.uge.net.medusa.data.NearCard

data class NearResponse(
    val cards : List<NearCard>
)
