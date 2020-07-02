package com.example.laba2.model

import android.content.ContentValues
import com.example.laba2.data.DatabaseHelper


class Card {
    var id: Int? = null
    var frontSide: String
    var backSide: String
    var dateForRepeat: Long = 0
    var interval: Long
    var repetition: Int
    var eFactor: Float

    constructor(frontSide: String, backSide: String) {
        this.frontSide = frontSide
        this.backSide = backSide
        repetition = 0
        interval = 0
        eFactor = 1.5f
    }

    constructor(
        id: Int,
        frontSide: String,
        backSide: String,
        dateForRepeat: Long,
        interval: Long,
        repetition: Int,
        EFactor: Float
    ) {
        this.frontSide = frontSide
        this.backSide = backSide
        this.dateForRepeat = dateForRepeat
        this.interval = interval
        this.repetition = repetition
        this.id = id
        eFactor = EFactor
    }

    override fun toString(): String {
        return "frontSide = $frontSide, backSide = $backSide"
    }

    fun toContentValuesForDeck(deckName: String): ContentValues {
        val contentValuesForCards = ContentValues()
        contentValuesForCards.put(DatabaseHelper.COLUMN_DATAFORREPEAT, dateForRepeat)
        contentValuesForCards.put(DatabaseHelper.COLUMN_FRONTSIDE, frontSide)
        contentValuesForCards.put(DatabaseHelper.COLUMN_BACKSIDE, backSide)
        contentValuesForCards.put(DatabaseHelper.COLUMN_DECK, deckName)
        contentValuesForCards.put(DatabaseHelper.COLUMN_INTERVAL, interval)
        contentValuesForCards.put(DatabaseHelper.COLUMN_EFACTOR, eFactor)
        contentValuesForCards.put(DatabaseHelper.COLUMN_REPEITION, repetition)
        return contentValuesForCards
    }

    fun toContentValues(): ContentValues {
        val contentValuesForCards = ContentValues()
        contentValuesForCards.put(DatabaseHelper.COLUMN_DATAFORREPEAT, dateForRepeat)
        contentValuesForCards.put(DatabaseHelper.COLUMN_FRONTSIDE, frontSide)
        contentValuesForCards.put(DatabaseHelper.COLUMN_BACKSIDE, backSide)
        contentValuesForCards.put(DatabaseHelper.COLUMN_INTERVAL, interval)
        contentValuesForCards.put(DatabaseHelper.COLUMN_EFACTOR, eFactor)
        contentValuesForCards.put(DatabaseHelper.COLUMN_REPEITION, repetition)
        return contentValuesForCards
    }


}
