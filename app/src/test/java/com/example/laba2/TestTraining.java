package com.example.laba2;



import com.example.laba2.model.Card;
import com.example.laba2.model.Deck;
import com.example.laba2.training.DegreeOfRemembering;
import com.example.laba2.training.TrainingImpl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestTraining {

    private Deck deck = new Deck();
    private TrainingImpl tr = new TrainingImpl();
    private Card card;
    private Card card2;

    @Before
    public void createDeck() {
        deck.addCardsInDeck("Cow", "Корова");
        deck.addCardsInDeck("Horse", "Лошадь");
        deck.addCardsInDeck("Shark", "Акула");
    }

    @Before
    public void addCards() {
        card = new Card("Crocodile", "Крокодил");
        card2 = new Card("Lion", "Лев");
    }


    @Test
    public void TestSelectCardForTraining() {
        TrainingImpl tr = new TrainingImpl();
        Deck expectedDeck = new Deck();
        deck.getCardsByIndex(0).setDateForRepeat(System.currentTimeMillis() + 100);
        deck.getCardsByIndex(1).setDateForRepeat(System.currentTimeMillis() - 100);
        expectedDeck.addCardsInDeck("Horse", "Лошадь");
        expectedDeck.addCardsInDeck("Shark", "Акула");
        tr.selectCardsForTraining(deck);
        for (int i = 0; i < tr.selectCardsForTraining(deck).cardsCount(); i++) {
            Assert.assertEquals(tr.selectCardsForTraining(deck).getCardsByIndex(i).toString(), expectedDeck.getCardsByIndex(i).toString());
        }
    }


    @Test(expected = NullPointerException.class)
    public void TrainingCardNotNull(){
        tr.trainingCards(null,null);
    }

    @Test
    public void TimeForRepeatInCaseUserDontRemember() {
        tr.trainingCards(card, DegreeOfRemembering.notRemember);
        Assert.assertEquals(Math.abs(card.getDateForRepeat() - System.currentTimeMillis()), 0);
    }

    @Test
    public void TimeForRepeatInCaseUserHardRemember() {
        tr.trainingCards(card, DegreeOfRemembering.hardRemember);
        Assert.assertTrue(card.getDateForRepeat() > System.currentTimeMillis());
    }

    @Test
    public void TimeForRepeatInCaseUserNormalRemember() {
        float easiness = 1.5F;
        int repetition = 2;
        int interval = 6;

        card.setRepetition(repetition);
        card.setInterval(interval);
        card.setEFactor(easiness);
        tr.trainingCards(card, DegreeOfRemembering.normalRemember);

        card2.setRepetition(repetition);
        card2.setInterval(interval);
        card2.setEFactor(easiness);
        tr.trainingCards(card2, DegreeOfRemembering.hardRemember);

        Assert.assertTrue(card.getDateForRepeat() > card2.getDateForRepeat());
    }

    @Test
    public void TimeForRepeatInCaseUserEasyRemember() {
        float easiness = 1.5F;
        int repetition = 2;
        int interval = 6;

        card.setRepetition(repetition);
        card.setInterval(interval);
        card.setEFactor(easiness);
        tr.trainingCards(card, DegreeOfRemembering.easyRemember);

        card2.setRepetition(repetition);
        card2.setInterval(interval);
        card2.setEFactor(easiness);
        tr.trainingCards(card2, DegreeOfRemembering.normalRemember);

        Assert.assertTrue(card.getDateForRepeat() > card2.getDateForRepeat());
    }

    @Test
    public void TimeForRepeatInCaseRepetitionEqualsZeroAndUserDontRemember(){
        int repetition = 0;

        card.setRepetition(repetition);
        tr.trainingCards(card, DegreeOfRemembering.notRemember);
        Assert.assertEquals(0, Math.abs(card.getDateForRepeat() - System.currentTimeMillis()));
    }

    @Test
    public void TimeForRepeatInCaseRepetitionEqualsZeroAndUserRemember(){
        int repetition = 0;

        card.setRepetition(repetition);
        tr.trainingCards(card, DegreeOfRemembering.easyRemember);
        Assert.assertEquals(86400,card.getDateForRepeat() - System.currentTimeMillis());
    }

    @Test
    public void TimeForRepeatInCaseRepetitionEqualsOneAndUserDontRemember(){
        int repetition = 1;

        card.setRepetition(repetition);
        tr.trainingCards(card, DegreeOfRemembering.notRemember);
        Assert.assertEquals(0, card.getDateForRepeat() - System.currentTimeMillis());
    }

    @Test
    public void TimeForRepeatInCaseRepetitionEqualsOneAndUserRemember(){
        int repetition = 1;

        card.setRepetition(repetition);
        tr.trainingCards(card, DegreeOfRemembering.easyRemember);
        Assert.assertEquals(518400, card.getDateForRepeat() - System.currentTimeMillis());
    }

    @Test
    public void TimeForRepeatInCaseRepetitionEqualsTwoAndUserDontRemember(){
        int repetition = 2;

        card.setRepetition(repetition);
        tr.trainingCards(card, DegreeOfRemembering.notRemember);
        Assert.assertEquals(0,card.getDateForRepeat() - System.currentTimeMillis());
    }


    @Test
    public void EasyFactorInCaseUserDontRemember() {
        card.setEFactor(1.5F);
        float EFactorBeforeTraining = card.getEFactor();
        tr.trainingCards(card, DegreeOfRemembering.notRemember);
        float EFactorAfterTraining = card.getEFactor();
        Assert.assertTrue(EFactorBeforeTraining > EFactorAfterTraining);
    }

    @Test
    public void EasyFactorInCaseUserHardRemember() {
        card.setEFactor(1.5F);
        float EFactorBeforeTraining = card.getEFactor();
        tr.trainingCards(card, DegreeOfRemembering.hardRemember);
        float EFactorAfterTraining = card.getEFactor();
        tr.trainingCards(card2, DegreeOfRemembering.notRemember);
        float EFactorForDontRemember = card2.getEFactor();
        Assert.assertTrue(EFactorBeforeTraining > EFactorAfterTraining && EFactorAfterTraining > EFactorForDontRemember);
    }

    @Test
    public void EasyFactorInCaseUserNormalRemember() {
        card.setEFactor(1.5F);
        float EFactorBeforeTraining = card.getEFactor();
        tr.trainingCards(card, DegreeOfRemembering.normalRemember);
        float EFactorAfterTraining = card.getEFactor();
        Assert.assertEquals(EFactorBeforeTraining, EFactorAfterTraining, 0);
    }

    @Test
    public void EasyFactorInCaseUserEasyRemember() {
        card.setEFactor(1.5F);
        float EFactorBeforeTraining = card.getEFactor();
        tr.trainingCards(card, DegreeOfRemembering.easyRemember);
        float EFactorAfterTraining = card.getEFactor();
        Assert.assertTrue(EFactorBeforeTraining < EFactorAfterTraining);
    }

    @Test
    public void EasyFactorNotLessMin() {
        float minEFactor = 1.3F;
        card.setEFactor(minEFactor);
        tr.trainingCards(card, DegreeOfRemembering.notRemember);
        tr.trainingCards(card, DegreeOfRemembering.notRemember);
        tr.trainingCards(card, DegreeOfRemembering.notRemember);
        Assert.assertEquals(minEFactor,card.getEFactor(),0);
    }

    @Test
    public void repetitionsForDontRememberAlwaysZero(){
        int repetitions = 100;
        card.setRepetition(repetitions);
        tr.trainingCards(card, DegreeOfRemembering.notRemember);
        Assert.assertEquals(0,card.getRepetition());
    }

    @Test
    public void repetitionsForUserRememberIncrement(){
        int repetitions = 5;
        card.setRepetition(repetitions);
        tr.trainingCards(card, DegreeOfRemembering.hardRemember);
        repetitions++;
        Assert.assertEquals(repetitions,card.getRepetition());
    }


}
