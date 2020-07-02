package com.example.laba2.training

import com.example.laba2.model.Card
import com.example.laba2.model.Deck

interface Training {
    fun selectCardsForTraining(deck: Deck?): Deck?
    fun trainingCards(card: Card?, degreeOfRemembering: DegreeOfRemembering?)
}