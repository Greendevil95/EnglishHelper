package com.example.laba2.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.text.TextUtils
import com.example.laba2.data.DatabaseHelper


class MyContentProvider: ContentProvider() {

    companion object {
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
        const val AUTHORITY = "com.laba2.MyContentProvider"
        const val DECK_PATH = "Decks"
        private var dbHelper: DatabaseHelper? = null
        val DECKS_URI: Uri = Uri.parse ("content://$AUTHORITY/$TABLE_DECKS")
        val CARDS_URI: Uri = Uri.parse ("content://$AUTHORITY/$TABLE_CARDS")

        private val DECKS = 1
        private val DECKS_ID = 2

        private val CARDS = 3
        private val CARDS_ID = 4

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        val projectionCard = arrayOf(COLUMN_ID, COLUMN_FRONTSIDE, COLUMN_BACKSIDE,
            COLUMN_DATAFORREPEAT, COLUMN_DECK, COLUMN_EFACTOR, COLUMN_INTERVAL, COLUMN_REPEITION)
        val projectionDeck = arrayOf(COLUMN_ID, COLUMN_DECKS)
    }


    init {
        uriMatcher.addURI(AUTHORITY, TABLE_DECKS, DECKS)
        uriMatcher.addURI(AUTHORITY, TABLE_DECKS + "/#",
            DECKS_ID)
        uriMatcher.addURI(AUTHORITY, TABLE_CARDS, CARDS)
        uriMatcher.addURI(AUTHORITY, TABLE_CARDS + "/#",
            CARDS_ID)
    }

    override fun onCreate(): Boolean {
        dbHelper = DatabaseHelper (context)
        return false
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db: SQLiteDatabase = dbHelper!!.writableDatabase
        var id: Long? = null
        return when (uriMatcher.match(uri)) {
            DECKS -> {
                id = db.insert(TABLE_DECKS, null, values)
                context.contentResolver.notifyChange(uri, null)
                Uri.parse("$DECKS_URI/$id")
            }

            CARDS -> {
                id = db.insert(TABLE_CARDS, null, values)
                context.contentResolver.notifyChange(uri, null)
                Uri.parse("$DECKS_URI/$id")
            }
            else -> throw java.lang.IllegalArgumentException("Unsupported URI: $uri")
        }

   }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val db = dbHelper!!.writableDatabase
        val queryBuilder = SQLiteQueryBuilder()
        queryBuilder.tables = TABLE_DECKS

        when (uriMatcher.match(uri)) {
            DECKS-> {
            }
            DECKS_ID-> {
                val id = uri.pathSegments[1]
                queryBuilder.appendWhere("$COLUMN_ID=$id")
            }
            CARDS-> {

            }
            else -> throw java.lang.IllegalArgumentException("Unsupported URI: $uri")
        }

        return queryBuilder.query(
            db, projection, selection,
            selectionArgs, null, null, sortOrder
        )
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        var newSelection = selection
        val db = dbHelper!!.writableDatabase
        when (uriMatcher.match(uri)) {
            DECKS_ID -> {
                val id = uri.pathSegments[1]
                val additionalSelection =
                    if (TextUtils.isEmpty(selection)) "" else " AND ($selection)"
                newSelection = "${COLUMN_ID}=$id${additionalSelection}"
                val updateCount = db.update(TABLE_DECKS, values, newSelection, selectionArgs)
                context.contentResolver.notifyChange(uri, null)
                return updateCount
            }
            CARDS_ID -> {
                val id = uri.pathSegments[1]
                val additionalSelection =
                    if (TextUtils.isEmpty(selection)) "" else " AND ($selection)"
                newSelection = "${COLUMN_ID}=$id${additionalSelection}"
                val updateCount = db.update(TABLE_CARDS, values, newSelection, selectionArgs)
                context.contentResolver.notifyChange(uri, null)
                return updateCount
            }
            else -> throw IllegalArgumentException("Unsupported URI: $uri")
        }
    }



    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>): Int {
        val db = dbHelper!!.writableDatabase
        var newSelection = selection
        when (uriMatcher.match(uri)) {
            DECKS -> {
                val deleteCount = db.delete(TABLE_DECKS, selection, selectionArgs)
                db.delete(TABLE_CARDS, selection, selectionArgs)
                context.contentResolver.notifyChange(uri, null)
                return deleteCount
            }
            DECKS_ID -> {
                val id = uri.pathSegments[1]
                newSelection = (COLUMN_ID + "=" + id
                        + if (!TextUtils.isEmpty(selection)) " AND ($selection)" else "")
                val deleteCount = db.delete(TABLE_DECKS, selection, selectionArgs)
                context.contentResolver.notifyChange(uri, null)
                return deleteCount
            }

            CARDS -> {
                //dbHelper!!.deleteCard(selectionArgs)
                val deleteCount = db.delete(TABLE_CARDS, selection, selectionArgs)
                context.contentResolver.notifyChange(uri, null)
                return deleteCount
            }
            else -> throw java.lang.IllegalArgumentException("Unsupported URI: $uri")
        }
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            DECKS -> "vnd.android.cursor.dir/vnd.com.com.laba2.MyContentProvider"
            CARDS -> "vnd.android.cursor.item/vnd.com.com.laba2.MyContentProvider"
            else -> throw java.lang.IllegalArgumentException("Unsupported URI: $uri")
        }
    }
}