package com.example.laba2

import com.example.laba2.model.Card
import com.example.laba2.model.Deck
import com.example.laba2.training.DegreeOfRemembering
import com.example.laba2.training.TrainingImpl

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class TestTraining {

    private val deck = Deck()
    private val tr = TrainingImpl()
    private var card: Card? = null
    private var card2: Card? = null

    @Before
    fun createDeck() {
        deck.addCardsInDeck("Cow", "Корова")
        deck.addCardsInDeck("Horse", "Лошадь")
        deck.addCardsInDeck("Shark", "Акула")
    }

    @Before
    fun addCards() {
        card = Card("Crocodile", "Крокодил")
        card2 = Card("Lion", "Лев")
    }


    @Test
    fun TestSelectCardForTraining() {
        val tr = TrainingImpl()
        val expectedDeck = Deck()
        deck.getCardsByIndex(0).dateForRepeat = System.currentTimeMillis() + 100
        deck.getCardsByIndex(1).dateForRepeat = System.currentTimeMillis() - 100
        expectedDeck.addCardsInDeck("Horse", "Лошадь")
        expectedDeck.addCardsInDeck("Shark", "Акула")
        tr.selectCardsForTraining(deck)
        for (i in 0 until tr.selectCardsForTraining(deck).cardsCount()) {
            Assert.assertEquals(
                tr.selectCardsForTraining(deck).getCardsByIndex(i).toString(),
                expectedDeck.getCardsByIndex(i).toString()
            )
        }
    }


    @Test(expected = NullPointerException::class)
    fun TrainingCardNotNull() {
        tr.trainingCards(null, null)
    }

    @Test
    fun TimeForRepeatInCaseUserDontRemember() {
        tr.trainingCards(card, DegreeOfRemembering.notRemember)
        Assert.assertEquals(Math.abs(card!!.dateForRepeat - System.currentTimeMillis()), 0)
    }

    @Test
    fun TimeForRepeatInCaseUserHardRemember() {
        tr.trainingCards(card, DegreeOfRemembering.hardRemember)
        Assert.assertTrue(card!!.dateForRepeat > System.currentTimeMillis())
    }

    @Test
    fun TimeForRepeatInCaseUserNormalRemember() {
        val easiness = 1.5f
        val repetition = 2
        val interval = 6

        card!!.repetition = repetition
        card!!.interval = interval.toLong()
        card!!.eFactor = easiness
        tr.trainingCards(card, DegreeOfRemembering.normalRemember)

        card2!!.repetition = repetition
        card2!!.interval = interval.toLong()
        card2!!.eFactor = easiness
        tr.trainingCards(card2, DegreeOfRemembering.hardRemember)

        Assert.assertTrue(card!!.dateForRepeat > card2!!.dateForRepeat)
    }

    @Test
    fun TimeForRepeatInCaseUserEasyRemember() {
        val easiness = 1.5f
        val repetition = 2
        val interval = 6

        card!!.repetition = repetition
        card!!.interval = interval.toLong()
        card!!.eFactor = easiness
        tr.trainingCards(card, DegreeOfRemembering.easyRemember)

        card2!!.repetition = repetition
        card2!!.interval = interval.toLong()
        card2!!.eFactor = easiness
        tr.trainingCards(card2, DegreeOfRemembering.normalRemember)

        Assert.assertTrue(card!!.dateForRepeat > card2!!.dateForRepeat)
    }

    @Test
    fun TimeForRepeatInCaseRepetitionEqualsZeroAndUserDontRemember() {
        val repetition = 0

        card!!.repetition = repetition
        tr.trainingCards(card, DegreeOfRemembering.notRemember)
        Assert.assertEquals(0, Math.abs(card!!.dateForRepeat - System.currentTimeMillis()))
    }

    @Test
    fun TimeForRepeatInCaseRepetitionEqualsZeroAndUserRemember() {
        val repetition = 0

        card!!.repetition = repetition
        tr.trainingCards(card, DegreeOfRemembering.easyRemember)
        Assert.assertEquals(86400, card!!.dateForRepeat - System.currentTimeMillis())
    }

    @Test
    fun TimeForRepeatInCaseRepetitionEqualsOneAndUserDontRemember() {
        val repetition = 1

        card!!.repetition = repetition
        tr.trainingCards(card, DegreeOfRemembering.notRemember)
        Assert.assertEquals(0, card!!.dateForRepeat - System.currentTimeMillis())
    }

    @Test
    fun TimeForRepeatInCaseRepetitionEqualsOneAndUserRemember() {
        val repetition = 1

        card!!.repetition = repetition
        tr.trainingCards(card, DegreeOfRemembering.easyRemember)
        Assert.assertEquals(518400, card!!.dateForRepeat - System.currentTimeMillis())
    }

    @Test
    fun TimeForRepeatInCaseRepetitionEqualsTwoAndUserDontRemember() {
        val repetition = 2

        card!!.repetition = repetition
        tr.trainingCards(card, DegreeOfRemembering.notRemember)
        Assert.assertEquals(0, card!!.dateForRepeat - System.currentTimeMillis())
    }


    @Test
    fun EasyFactorInCaseUserDontRemember() {
        card!!.eFactor = 1.5f
        val EFactorBeforeTraining = card!!.eFactor
        tr.trainingCards(card, DegreeOfRemembering.notRemember)
        val EFactorAfterTraining = card!!.eFactor
        Assert.assertTrue(EFactorBeforeTraining > EFactorAfterTraining)
    }

    @Test
    fun EasyFactorInCaseUserHardRemember() {
        card!!.eFactor = 1.5f
        val EFactorBeforeTraining = card!!.eFactor
        tr.trainingCards(card, DegreeOfRemembering.hardRemember)
        val EFactorAfterTraining = card!!.eFactor
        tr.trainingCards(card2, DegreeOfRemembering.notRemember)
        val EFactorForDontRemember = card2!!.eFactor
        Assert.assertTrue(EFactorBeforeTraining > EFactorAfterTraining && EFactorAfterTraining > EFactorForDontRemember)
    }

    @Test
    fun EasyFactorInCaseUserNormalRemember() {
        card!!.eFactor = 1.5f
        val EFactorBeforeTraining = card!!.eFactor
        tr.trainingCards(card, DegreeOfRemembering.normalRemember)
        val EFactorAfterTraining = card!!.eFactor
        Assert.assertEquals(EFactorBeforeTraining, EFactorAfterTraining, 0f)
    }

    @Test
    fun EasyFactorInCaseUserEasyRemember() {
        card!!.eFactor = 1.5f
        val EFactorBeforeTraining = card!!.eFactor
        tr.trainingCards(card, DegreeOfRemembering.easyRemember)
        val EFactorAfterTraining = card!!.eFactor
        Assert.assertTrue(EFactorBeforeTraining < EFactorAfterTraining)
    }

    @Test
    fun EasyFactorNotLessMin() {
        val minEFactor = 1.3f
        card!!.eFactor = minEFactor
        tr.trainingCards(card, DegreeOfRemembering.notRemember)
        tr.trainingCards(card, DegreeOfRemembering.notRemember)
        tr.trainingCards(card, DegreeOfRemembering.notRemember)
        Assert.assertEquals(minEFactor, card!!.eFactor, 0f)
    }

    @Test
    fun repetitionsForDontRememberAlwaysZero() {
        val repetitions = 100
        card!!.repetition = repetitions
        tr.trainingCards(card, DegreeOfRemembering.notRemember)
        Assert.assertEquals(0, card!!.repetition.toLong())
    }

    @Test
    fun repetitionsForUserRememberIncrement() {
        var repetitions = 5
        card!!.repetition = repetitions
        tr.trainingCards(card, DegreeOfRemembering.hardRemember)
        repetitions++
        Assert.assertEquals(repetitions.toLong(), card!!.repetition.toLong())
    }


}
