package com.example.laba2.activities

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.laba2.R
import com.example.laba2.data.DatabaseHelper
import com.example.laba2.model.Card
import com.example.laba2.provider.MyContentProvider
import java.util.*

class AddDeckActivity : AppCompatActivity() {
    private var deckName: EditText? = null
    private var dbHelper: DatabaseHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_deck)
        deckName = findViewById(R.id.editDeck)
        dbHelper = DatabaseHelper(this)
    }

    fun createDeck(view: View?) {
        if (deckName!!.text.toString() == "") {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Поле не должно быть пустым!")
                .setCancelable(false)
                .setNegativeButton("ОК"
                ) { dialog, id -> dialog.cancel() }
            val alert: AlertDialog = builder.create()
            alert.show()
        } else {
            dbHelper!!.openDataBase()
            val arrayList = dbHelper!!.getAllDecks()
            if (arrayList.contains(deckName!!.text.toString())) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                val alert: AlertDialog = builder.create()
                builder.setTitle("Данная колода уже существует!")
                    .setCancelable(false)
                    .setNegativeButton("ОК"
                    ) { dialog, id -> dialog.cancel() }
                alert.show()
            } else {
                val content = ContentValues()
                content.put(DatabaseHelper.COLUMN_DECKS, deckName!!.text.toString())
                contentResolver.insert(MyContentProvider.DECKS_URI, content)
                //dbHelper!!.addDeck(deckName!!.text.toString())
                dbHelper!!.close()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}