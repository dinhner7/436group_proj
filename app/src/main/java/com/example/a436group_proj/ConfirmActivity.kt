package com.example.a436group_proj

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ConfirmActivity: AppCompatActivity() {
    private lateinit var itemsTV: TextView
    private lateinit var totalPriceTV: TextView
    private lateinit var confirmButton: Button
    private lateinit var backButton: Button
    private lateinit var rating: RatingBar
    private lateinit var discountInfoTV: TextView
    private lateinit var cardname: EditText
    private lateinit var cardnumber: EditText
    private lateinit var cardexpire: EditText
    private lateinit var cardcvv: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)

        itemsTV = findViewById(R.id.items)
        totalPriceTV = findViewById(R.id.totalPrice)
        confirmButton = findViewById(R.id.confirmButton)
        backButton = findViewById(R.id.backButton)
        rating = findViewById(R.id.ratingBar)
        discountInfoTV = findViewById(R.id.discountInfo)

        cardname = findViewById(R.id.cardName)
        val cardNameString = cardnumber.text.toString()
        cardnumber = findViewById(R.id.cardNumber)
        val cardNumberString = cardnumber.text.toString()
        cardexpire = findViewById(R.id.cardExpire)
        val cardExpireString = cardexpire.text.toString()
        cardcvv = findViewById(R.id.cardCVV)
        val cardCVVString = cardcvv.text.toString()


        val order = HomeActivity.order
        displayorder(order)

        val hasDiscount = checkForDiscount()
        if (hasDiscount) {
            Toast.makeText(
                this,
                "You've earned a discount for a previous 5-star rating!",
                Toast.LENGTH_LONG
            ).show()
            order.applyDiscount(10) // 10% 할인
        }


        rating.setOnRatingBarChangeListener { _, rating, _ ->
            if (rating == 5.0f) {
                discountInfoTV.text = "Discount: 10%"
            }
        }


        confirmButton.setOnClickListener {

            if (cardNameString.isEmpty() || cardNumberString.isEmpty() ||
                cardExpireString.isEmpty() || cardCVVString.isEmpty()) {
                Toast.makeText(this, "Please fill out all credit info fields.",
                    Toast.LENGTH_SHORT).show()
            } else{
                saveRating(rating.rating)
                saveCreditInfo(cardNameString, cardNumberString, cardExpireString, cardCVVString)
                Toast.makeText(this, "Thank you for your order!", Toast.LENGTH_SHORT).show()
                finish()
            }

        }

        backButton.setOnClickListener {
            val intent = Intent(this, Customize::class.java)
            startActivity(intent)
            finish()
        }
    }


        private fun displayorder(order: Order) {
            val pizzas = order.getOrder()
            val totalPrice = order.getTotalPrice()

            // 주문 요약 표시
            val summary = StringBuilder()
            pizzas.forEachIndexed { index, pizza ->
                summary.append("Items:\n")
                summary.append("Pizza ${index + 1}:\n")
                summary.append("Size: ${pizza.size}\n")
                summary.append("Crust: ${pizza.crust}\n")
                summary.append("Cheese: ${pizza.cheese}\n")
                summary.append("Toppings: ${pizza.toppings.joinToString(", ")}\n")
                summary.append("Price: $${pizza.getTotalCost()}\n\n")
            }
            itemsTV.text = summary.toString()


            totalPriceTV.text = "Total Price: $${"%.2f".format(totalPrice)}"
        }

        private fun saveRating(rating: Float) {
            if (rating == 5.0f) {
                val sharedPref = getSharedPreferences("RatingData", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putBoolean("hasGivenFiveStars", true)
                    apply()
                }
            }
        }

        private fun checkForDiscount(): Boolean {
            val sharedPref = getSharedPreferences("RatingData", Context.MODE_PRIVATE)
            return sharedPref.getBoolean("hasGivenFiveStars", false)
        }

        private fun saveCreditInfo(name:String,
                                   number:String, expire:String, cvv:String){
            val sharedPref = getSharedPreferences("CreditInfo",Context.MODE_PRIVATE)
                with(sharedPref.edit()){
                    putString("CardName", name)
                    putString("CardNumber", number)
                    putString("CardExpiry", expire)
                    putString("CardCVV", cvv)
                    apply()
                }
        }

}

