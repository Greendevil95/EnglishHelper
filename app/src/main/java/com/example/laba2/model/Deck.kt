package com.example.laba2.model

import java.util.*

class Deck {

    var deckOfCards = ArrayList<Card>()
    fun addCardsInDeck(frontSide: String?, backSide: String?) {
        deckOfCards.add(Card(frontSide!!, backSide!!))
    }

    fun addCardsInDeckFromBD(
        id: Int?,
        frontSide: String?,
        backSide: String?,
        dataForRepeat: Long,
        interval: Long,
        repetition: Int,
        EFactor: Float
    ) {
        deckOfCards.add(
            Card(id!!,
                frontSide!!,
                backSide!!, dataForRepeat, interval, repetition, EFactor
            )
        )
    }

    fun takeRandomCardForTraining(): Card? {
        return if (deckOfCards.size > 0) {
            val rnd = Random().nextInt(deckOfCards.size)
            deckOfCards[rnd]
        } else null
    }

    fun cardsCount(): Int {
        return deckOfCards.size
    }

    fun getCardsByIndex(index: Int): Card {
        return deckOfCards[index]
    }
}