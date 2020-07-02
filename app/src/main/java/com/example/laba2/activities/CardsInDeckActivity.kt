package com.example.laba2.activities

import android.R.id
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.laba2.R
import com.example.laba2.data.DatabaseHelper
import com.example.laba2.provider.MyContentProvider


class CardsInDeckActivity : AppCompatActivity() {
    private var cardsInDecks: ListView? = null
    private var dbHelper: DatabaseHelper? = null
    private var adapter: ArrayAdapter<Any>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cards_in_deck)
        cardsInDecks = findViewById(R.id.CardsInDeckList)
        fillDeck()
        cardsInDecks!!.onItemLongClickListener =
            OnItemLongClickListener { parent, view, position, id ->
                showPopupMenu(view)
                true
            }
    }

    private fun fillDeck() {
        dbHelper = DatabaseHelper(this)
        dbHelper!!.openDataBase()
        val intent: Intent = intent
        val deckName = intent.getStringExtra("name_of_deck")
        val cards = dbHelper!!.getCardsInDecks(deckName)

        adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,
            cards as List<Any>
        )
        cardsInDecks!!.adapter = adapter
        dbHelper!!.close()
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.popupcardmenu)
        popupMenu
            .setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.deleteCard -> {
                        deleteCard(view)
                        true
                    }
                    else -> false
                }
            }
        popupMenu.show()
    }

    private fun deleteCard(view: View) {
        val textView = view as TextView
        val strText = textView.text.toString()
        val dbString = strText.substring(0, strText.indexOf(" "))
        dbHelper!!.openDataBase()
        contentResolver.delete(MyContentProvider.CARDS_URI,MyContentProvider.COLUMN_FRONTSIDE + "=?",
            arrayOf(dbString))
       //dbHelper!!.deleteCard(dbString)
        dbHelper!!.close()
        adapter!!.remove(strText)
        adapter!!.notifyDataSetChanged()
    }
}
