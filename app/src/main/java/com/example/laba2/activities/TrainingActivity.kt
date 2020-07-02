package com.example.laba2.activities

import android.R.id
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.laba2.R
import com.example.laba2.data.DatabaseHelper
import com.example.laba2.model.Card
import com.example.laba2.model.Deck
import com.example.laba2.provider.MyContentProvider
import com.example.laba2.training.DegreeOfRemembering
import com.example.laba2.training.TrainingImpl


class TrainingActivity : AppCompatActivity() {
    private val deck: Deck = Deck()
    private val tr: TrainingImpl = TrainingImpl()
    private var deckForRepeat: Deck = Deck()
    private var cardForRemember: Card? = null
    private var dbHelper: DatabaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)
        frontSideView = findViewById(R.id.frontSide)
        backSideView = findViewById(R.id.backSide)
        fillDeck(deck)
        deckForRepeat = tr.selectCardsForTraining(deck)
        cardForRemember = deckForRepeat.takeRandomCardForTraining()
        if (cardForRemember != null) {
            frontSideView!!.text = cardForRemember!!.frontSide
            backSideView!!.text = cardForRemember!!.backSide
        } else {
            frontSideView!!.visibility = View.INVISIBLE
            backSideView!!.visibility = View.INVISIBLE
            val builder: AlertDialog.Builder =
                AlertDialog.Builder(this)
            builder.setTitle("Нет карт которым нужна тернировка!")
                .setCancelable(false)
                .setNegativeButton("ОК",
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                        startActivity(Intent(this, MainActivity::class.java))
                    })
            val alert: AlertDialog = builder.create()
            alert.show()
        }
    }

    private var frontSideView: TextView? = null
    private var backSideView: TextView? = null

    fun fillDeck(deck: Deck) {
        dbHelper = DatabaseHelper(this)
        dbHelper!!.openDataBase()
        val intent: Intent = intent
        val deckName = intent.getStringExtra("name_of_deck")
        dbHelper!!.getCardsForTraining(deckName,deck)
        dbHelper!!.close()
    }

    fun onClickShowAnswer(view: View?) {
        backSideView!!.visibility = View.VISIBLE
    }

    fun addAndShowNextCard() {
        saveCardsIntoDb(cardForRemember)
        deckForRepeat = tr.selectCardsForTraining(deck)
        if (deckForRepeat.deckOfCards.size > 0) {
            cardForRemember = deckForRepeat.takeRandomCardForTraining()
            if (deckForRepeat.deckOfCards.size >= 1) {
                frontSideView!!.text = cardForRemember!!.frontSide
                backSideView!!.visibility = View.INVISIBLE
                backSideView!!.text = cardForRemember!!.backSide
            }
        } else {
            val builder: AlertDialog.Builder =
                AlertDialog.Builder(this)
            builder.setTitle("Тренировка окончена!")
                .setCancelable(false)
                .setNegativeButton("ОК",
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                        startActivity(Intent(this, MainActivity::class.java))
                    })
            val alert: AlertDialog = builder.create()
            alert.show()
        }
    }

    fun saveCardsIntoDb(card: Card?) {
        dbHelper!!.openDataBase()
        if (card != null) {
            val uri: Uri = Uri.parse(MyContentProvider.CARDS_URI.toString() + "/" + card.id)
            val contentValuesForCards = card.toContentValues()
            contentResolver.update(uri,contentValuesForCards,DatabaseHelper.COLUMN_FRONTSIDE + "=?",arrayOf(java.lang.String.valueOf(card.frontSide)) )
            //dbHelper!!.updateCard(card)
        }
        dbHelper!!.close()
    }

    fun onClickDontRemember(view: View?) {
        tr.trainingCards(cardForRemember, DegreeOfRemembering.notRemember)
        addAndShowNextCard()
    }

    fun onClickHard(view: View?) {
        tr.trainingCards(cardForRemember, DegreeOfRemembering.hardRemember)
        addAndShowNextCard()
    }

    fun onClickNormal(view: View?) {
        tr.trainingCards(cardForRemember, DegreeOfRemembering.normalRemember)
        addAndShowNextCard()
    }

    fun onClickEasy(view: View?) {
        tr.trainingCards(cardForRemember, DegreeOfRemembering.easyRemember)
        addAndShowNextCard()
    }
}