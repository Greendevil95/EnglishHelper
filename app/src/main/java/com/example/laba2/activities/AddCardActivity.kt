package com.example.laba2.activities

import android.R.attr.name
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.laba2.R
import com.example.laba2.data.DatabaseHelper
import com.example.laba2.model.Card
import com.example.laba2.provider.MyContentProvider
import kotlinx.android.synthetic.main.card_activity.*


class AddCardActivity : AppCompatActivity() {
    private var dbHelper: DatabaseHelper? = null
    private var adapter: ArrayAdapter<*>? = null
    private var spinner: Spinner? = null
    private var editFrontSIde: EditText? = null
    private var editBackSide: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.card_activity)


        dbHelper = DatabaseHelper(this)
        dbHelper!!.openDataBase()
        val decks1 = ArrayList<String>()
        val cursor = contentResolver.query(
            MyContentProvider.DECKS_URI, MyContentProvider.projectionDeck, null, null,
            null
        )
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val deckName =
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DECKS))
            decks1.add(deckName)
            cursor.moveToNext()
        }
        cursor.close()
        //val decks = dbHelper!!.getAllDecks()
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,
            decks1 as List<Any?>
        )
        adapter!!.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinner = this.DecksSpinner
        spinner!!.adapter = adapter
    }

    fun addCard(view: View?) {
        editFrontSIde = findViewById(R.id.editFrontSide)
        editBackSide = findViewById(R.id.editBackSide)
        if (editFrontSIde!!.text.toString() == "" || editBackSide!!.text.toString() == "") {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Поля не должны быть пустыми!")
                .setCancelable(false)
                .setNegativeButton("ОК"
                ) { dialog, id -> dialog.cancel() }
            val alert: AlertDialog = builder.create()
            alert.show()
        } else {
            dbHelper!!.openDataBase()
            val decks1 = ArrayList<String>()
            val cursor = contentResolver.query(
                MyContentProvider.DECKS_URI, MyContentProvider.projectionDeck, null, null,
                null)
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val deckName =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DECKS))
                decks1.add(deckName)
                cursor.moveToNext()
            }
            cursor.close()
            //val arrayList = dbHelper!!.getAllCards()
            if (decks1.contains(editFrontSIde!!.text.toString())) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle("Данная карточка уже есть в колоде!")
                    .setCancelable(false)
                    .setNegativeButton("ОК"
                    ) { dialog, id -> dialog.cancel() }
                val alert: AlertDialog = builder.create()
                alert.show()
                dbHelper!!.close()
            } else {
                val card =
                    Card(editFrontSIde!!.text.toString(), editBackSide!!.text.toString())
                val contentValuesForCards = card.toContentValuesForDeck(spinner!!.selectedItem.toString())
                contentResolver.insert(MyContentProvider.CARDS_URI, contentValuesForCards)
                //dbHelper!!.addCardInDeck(card,spinner!!.selectedItem.toString())
                dbHelper!!.close()
                editFrontSIde!!.setText("")
                editBackSide!!.setText("")
            }
        }
    }
}