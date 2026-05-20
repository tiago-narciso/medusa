package fr.uge.net.medusa.data

data class CardsCollection(
    val name: String,
    val cardCount: Int,
    val power: Int,
    val cards: List<Card>
)