package com.example.laba2.data

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import com.example.laba2.model.Card
import com.example.laba2.model.Deck
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(
        context,
        DB_NAME,
        null,
        DB_VERSION
    ) {
    private var mDataBase: SQLiteDatabase? = null
    private var mNeedUpdate = false
    private val mContext: Context
    private val myCR: ContentResolver

    fun updateDataBase() {
        if (mNeedUpdate) {
            val dbFile =
                File(DB_PATH + DB_NAME)
            if (dbFile.exists()) dbFile.delete()
            copyDataBase()
            mNeedUpdate = false
        }
    }

    private fun checkDataBase(): Boolean {
        val dbFile =
            File(DB_PATH + DB_NAME)
        return dbFile.exists()
    }

    private fun copyDataBase() {
        if (!checkDataBase()) {
            this.readableDatabase
            this.close()
            try {
                copyDBFile()
            } catch (mIOException: IOException) {
                throw Error("ErrorCopyingDataBase")
            }
        }
    }

    @Throws(IOException::class)
    private fun copyDBFile() {
        val mInput =
            mContext.assets.open(DB_NAME)
        //InputStream mInput = mContext.getResources().openRawResource(R.raw.info);
        val mOutput: OutputStream =
            FileOutputStream(DB_PATH + DB_NAME)
        val mBuffer = ByteArray(1024)
        var mLength: Int
        while (mInput.read(mBuffer).also { mLength = it } > 0) mOutput.write(mBuffer, 0, mLength)
        mOutput.flush()
        mOutput.close()
        mInput.close()
    }

    fun deleteDeck(deckName: String){
        mDataBase!!.delete(
            TABLE_DECKS,
            COLUMN_DECKS + "=?",
            arrayOf(deckName))
        mDataBase!!.delete(
            TABLE_CARDS,
            COLUMN_DECK + "=?",
            arrayOf(deckName))
    }


    fun deleteCard(cardName: Array<String>){
        mDataBase!!.delete(TABLE_CARDS, COLUMN_FRONTSIDE + "=?",
            cardName
        )
    }

    fun updateCard(card: Card){
        val contentValuesForCards = ContentValues()
        contentValuesForCards.put(COLUMN_DATAFORREPEAT, card.dateForRepeat)
        contentValuesForCards.put(COLUMN_FRONTSIDE, card.frontSide)
        contentValuesForCards.put(COLUMN_BACKSIDE, card.backSide)
        contentValuesForCards.put(COLUMN_INTERVAL, card.interval)
        contentValuesForCards.put(COLUMN_EFACTOR, card.eFactor)
        contentValuesForCards.put(COLUMN_REPEITION, card.repetition)
        mDataBase!!.update(
            TABLE_CARDS,
            contentValuesForCards,
            COLUMN_FRONTSIDE + "=?",
            arrayOf(java.lang.String.valueOf(card.frontSide))
        )
    }

    fun getCardsForTraining(deckName: String, deck: Deck): Deck{
        val cursor =
            mDataBase!!.rawQuery("SELECT * FROM cards WHERE deck = '$deckName'", null)
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val frontSide = cursor.getString(1)
            val backSide = cursor.getString(2)
            val dataForRepeat = cursor.getLong(3)
            val interval = cursor.getLong(4)
            val repetition = cursor.getInt(5)
            val EFactor = cursor.getFloat(6)
            val id = cursor.getInt(7)
            deck.addCardsInDeckFromBD(
                id,
                frontSide,
                backSide,
                dataForRepeat,
                interval,
                repetition,
                EFactor
            )
            cursor.moveToNext()
        }
        cursor.close()
        return deck
    }


    @Throws(SQLException::class)
    fun openDataBase(): Boolean {
        mDataBase = SQLiteDatabase.openDatabase(
            DB_PATH + DB_NAME,
            null,
            SQLiteDatabase.CREATE_IF_NECESSARY
        )
        return mDataBase != null
    }

    fun addCardInDeck(card: Card, deckName: String){
        var contentValuesForCards = ContentValues()
        contentValuesForCards.put(COLUMN_DATAFORREPEAT, card.dateForRepeat)
        contentValuesForCards.put(COLUMN_FRONTSIDE, card.frontSide)
        contentValuesForCards.put(COLUMN_BACKSIDE, card.backSide)
        contentValuesForCards.put(COLUMN_DECK, deckName)
        contentValuesForCards.put(COLUMN_INTERVAL, card.interval)
        contentValuesForCards.put(COLUMN_EFACTOR, card.eFactor)
        contentValuesForCards.put(COLUMN_REPEITION, card.repetition)
        mDataBase!!.insert(TABLE_CARDS, null, contentValuesForCards)
    }

    fun addDeck(deckName: String) {
        val values = ContentValues()
        values.put(COLUMN_DECKS, deckName)
        mDataBase!!.insert(TABLE_DECKS,null, values)
        //myCR.insert(MyContentProvider.DECKS_URI, values)
    }
    
    fun getAllDecks(): ArrayList<String>{
        val decksList = ArrayList<String>()
        val cursor = mDataBase!!.rawQuery("SELECT * FROM decks", null)
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val str = cursor.getString(1)
            decksList.add(str)
            cursor.moveToNext()
        }
        cursor.close()
        return decksList
    }
    
    fun getAllCards(): ArrayList<String>{
        val arrayList = ArrayList<String>()
        val cursor = mDataBase!!.rawQuery("SELECT * FROM cards", null)
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val str = cursor.getString(1)
            arrayList.add(str)
            cursor.moveToNext()
        }
        cursor.close()
    return arrayList
    }

    fun getCardsInDecks(deckName: String): ArrayList<String>{
        val cards = ArrayList<String>()
        val cursor =
            mDataBase!!.rawQuery("SELECT * FROM cards WHERE deck = '$deckName'", null)
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val frontSide = cursor.getString(1)
            val backSide = cursor.getString(2)
            cards.add("$frontSide - $backSide")
            cursor.moveToNext()
        }
        cursor.close()
        return cards
    }

    @Synchronized
    override fun close() {
        if (mDataBase != null) mDataBase!!.close()
        super.close()
    }

    override fun onCreate(db: SQLiteDatabase) {
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        if (newVersion > oldVersion) mNeedUpdate = true
    }

    companion object {
        private var DB_PATH = "" // полный путь к базе данных
        private const val DB_NAME = "decks.db"
        private const val DB_VERSION = 13 // версия базы данных
        const val TABLE_CARDS = "Cards" // название таблицы в бд
        const val TABLE_DECKS = "Decks"
        // названия столбцов
        const val COLUMN_ID = "_id"
        const val COLUMN_FRONTSIDE = "frontSide"
        const val COLUMN_BACKSIDE = "backSide"
        const val COLUMN_DECK = "Deck"
        const val COLUMN_DATAFORREPEAT = "DataForRepeat"
        const val COLUMN_INTERVAL = "Interval"
        const val COLUMN_REPEITION = "Repetition"
        const val COLUMN_EFACTOR = "EFactor"
        const val COLUMN_DECKS = "Decks"
    }

    init {
        myCR = context.contentResolver
        DB_PATH =
            if (Build.VERSION.SDK_INT >= 19) context.getApplicationInfo().dataDir + "/databases/" else "/data/data/" + context.getPackageName() + "/databases/"
        mContext = context
        copyDataBase()
        this.readableDatabase
    }
}