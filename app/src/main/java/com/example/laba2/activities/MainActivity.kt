package com.example.laba2.activities

import android.content.DialogInterface
import android.content.Intent
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.laba2.R
import com.example.laba2.data.DatabaseHelper
import com.example.laba2.provider.MyContentProvider
import java.io.IOException
import java.util.*

class MainActivity: AppCompatActivity() {
    var dbHelper: DatabaseHelper? = null
    var mDb: SQLiteDatabase? = null
    var adapter: ArrayAdapter<Any>? = null
    var decks: ArrayList<String>? = null

    var decksList: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        decksList = this.findViewById(R.id.decksList)
        dbHelper = DatabaseHelper(this)
        dbHelper!!.openDataBase()
        decks  = dbHelper!!.getAllDecks()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, decks as List<Any>)
        decksList!!.adapter = adapter
        dbHelper!!.close()
        decksList!!.onItemClickListener =
            OnItemClickListener { parent, itemClicked, position, id ->
                val textView = itemClicked as TextView
                val strText = textView.text.toString()
                val intent = Intent(this, TrainingActivity::class.java)
                intent.putExtra("name_of_deck", strText)
                startActivity(intent)
            }
        decksList!!.onItemLongClickListener =
            OnItemLongClickListener { parent, view, position, id ->
                showPopupMenu(view)
                true
            }
    }

     private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.popupmenu)
        popupMenu
            .setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.ShowCards -> {
                        val textView = view as TextView
                        val strText = textView.text.toString()
                        val intent =
                            Intent(this, CardsInDeckActivity::class.java)
                        intent.putExtra("name_of_deck", strText)
                        startActivity(intent)
                        true
                    }
                    R.id.deleteDeck -> {
                        deleteDeck(view)
                        true
                    }
                    else -> false
                }
            }
        popupMenu.show()
    }

    fun deleteDeck(view: View) {
        val textView = view as TextView
        val strText = textView.text.toString()
        dbHelper!!.openDataBase()
        contentResolver.delete(
            MyContentProvider.DECKS_URI,MyContentProvider.COLUMN_DECKS + "=?",
            arrayOf(strText))
        //dbHelper!!.deleteDeck(strText)
        dbHelper!!.close()
        adapter!!.remove(strText)
        adapter!!.notifyDataSetChanged()
    }


    fun addCard(view: View?) {
        if (decksList!!.count > 0) {
            val intent = Intent(this, AddCardActivity::class.java)
            startActivity(intent)
        } else {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Для добавления карточек, нужно создать колоду!")
                .setCancelable(false)
                .setNegativeButton("ОК",
                    DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
            val alert: AlertDialog = builder.create()
            alert.show()
        }
    }

    fun addDeck(view: View?) {
        val intent = Intent(this, AddDeckActivity::class.java)
        startActivity(intent)
    }

}
