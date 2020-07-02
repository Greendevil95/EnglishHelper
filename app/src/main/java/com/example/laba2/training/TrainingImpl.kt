package com.example.laba2.training

import com.example.laba2.model.Card
import com.example.laba2.model.Deck
import kotlin.math.roundToLong

class TrainingImpl : Training {

    private var quality = 0

    override fun selectCardsForTraining(deck: Deck?): Deck {
        return if (deck != null) {
            val now = System.currentTimeMillis()
            val deckForRepeat = Deck()
            for (i in 0 until deck.deckOfCards.size) {
                val dateForRepeat: Long = deck.deckOfCards[i].dateForRepeat
                if (now >= dateForRepeat) {
                    deckForRepeat.deckOfCards.add(deck.deckOfCards[i])
                }
            }
            deckForRepeat
        } else throw NullPointerException("Can't training empty deck!")
    }

    override fun trainingCards(card: Card?, degreeOfRemembering: DegreeOfRemembering?) {
        if (card != null) {
            when (degreeOfRemembering) {
                DegreeOfRemembering.notRemember -> quality = 0
                DegreeOfRemembering.hardRemember -> quality = 1
                DegreeOfRemembering.normalRemember -> quality = 2
                DegreeOfRemembering.easyRemember -> quality = 3
            }
            //System.out.println("Значение оценки " + quality);
            require(!(quality < 0 || quality > 3)) { "Illegal argument for quality!" }
            var repetitions: Int = card.repetition
            var easiness: Float = card.eFactor
            var interval: Long = card.interval
            // easiness factor
            easiness = Math.max(
                1.3,
                easiness + 0.1 - (3.0 - quality) * (0.08 + (3.0 - quality) * 0.02)
            ).toFloat()
            /*System.out.println("Easy factor " + easiness);*/ // repetitions
            if (quality == 0) {
                repetitions = 0
            } else {
                repetitions += 1
            }
            // interval
            interval = if (repetitions == 0) {
                0
            } else if (repetitions == 1) {
                1
            } else if (repetitions == 2) {
                6
            } else {
                (interval * easiness).roundToLong()
            }
            /*System.out.println("Значение повторов " + repetitions);
        System.out.println("Значение интервала " + interval);*/
// next practice
            val secondsInDay = 60 * 60 * 24
            val now = System.currentTimeMillis()
            val nextPracticeDate = now + secondsInDay * interval
            /*System.out.println("Сейчас " + now);
        System.out.println("Следующий повтор " + nextPracticeDate);
        System.out.println((nextPracticeDate - now));
        System.out.println(secondsInDay * interval);*/
// Store the nextPracticeDate in the database
            card.repetition = repetitions
            card.eFactor = easiness
            card.interval = interval
            card.dateForRepeat = nextPracticeDate
        } else throw NullPointerException("Can't training empty card!")
    }
}