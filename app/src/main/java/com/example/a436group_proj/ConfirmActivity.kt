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
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class ConfirmActivity: AppCompatActivity() {
    private lateinit var itemsTV: TextView
    private lateinit var totalPriceTV: TextView
    private lateinit var confirmButton: AppCompatButton
    private lateinit var backButton: AppCompatButton
    private lateinit var rating: RatingBar
    private lateinit var discountInfoTV: TextView
    private lateinit var cardname: EditText
    private lateinit var cardnumber: EditText
    private lateinit var cardexpire: EditText
    private lateinit var cardcvv: EditText
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)

        database = FirebaseDatabase.getInstance().reference.child("users")

        itemsTV = findViewById(R.id.items)
        totalPriceTV = findViewById(R.id.totalPrice)
        confirmButton = findViewById(R.id.confirmButton)
        backButton = findViewById(R.id.backButton)
        rating = findViewById(R.id.ratingBar)
        discountInfoTV = findViewById(R.id.discountInfo)

        cardname = findViewById(R.id.cardName)
        cardnumber = findViewById(R.id.cardNumber)
        cardexpire = findViewById(R.id.cardExpire)
        cardcvv = findViewById(R.id.cardCVV)

        //Retrieves email to save order or redirects to login if not logged in
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val email = sharedPreferences.getString("email", null)
        if (email == null) {
            Toast.makeText(this, "User email not found. Please log in again.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        val order = HomeActivity.order
        displayorder(order)
        displayCreditInfo()

        val hasDiscount = checkForDiscount()
        if (hasDiscount) {
            Toast.makeText(
                this,
                "You've earned a discount for a previous 5-star rating!",
                Toast.LENGTH_LONG
            ).show()
            order.applyDiscount(10) 
        }


        rating.setOnRatingBarChangeListener { _, rating, _ ->
            if (rating == 5.0f) {
                discountInfoTV.text = "Discount: 10%"
            }
            Toast.makeText(this, "Thank you for rating our small business!",
                Toast.LENGTH_SHORT).show()
        }


        confirmButton.setOnClickListener {

            val cardNameString = cardname.text.toString()
            val cardNumberString = cardnumber.text.toString()
            val cardExpireString = cardexpire.text.toString()
            val cardCVVString = cardcvv.text.toString()
            if (cardNameString.isEmpty() || cardNumberString.isEmpty() ||
                cardExpireString.isEmpty() || cardCVVString.isEmpty()) {
                Toast.makeText(this, "Please fill out all credit info fields.",
                    Toast.LENGTH_SHORT).show()
            } else{
                saveRating(rating.rating)
                saveCreditInfo(cardNameString, cardNumberString, cardExpireString, cardCVVString)

                // Save the order to Firebase
                if (email != null) {
                    saveOrderToFirebase(order, email)
                } else {
                    Toast.makeText(this, "User email not found", Toast.LENGTH_SHORT).show()
                }
                
                HomeActivity.order.clearOrder()
                Toast.makeText(this, "Thank you for your order! A confirmation email was sent to you.", Toast.LENGTH_SHORT).show()

                // send email
                var email : String = ""
                sendEmail(order, email)

                finish()
            }

        }

        backButton.setOnClickListener {
            //val intent = Intent(this, HomeActivity::class.java)
            //startActivity(intent)
            finish()
        }
    }

    private fun saveOrderToFirebase(order: Order, email: String) {
        val safeEmail = email.replace(".", ",")
        val userOrdersRef = database.child(safeEmail).child("orders")

        // Create a unique key for the new order
        val newOrderRef = userOrdersRef.push()

        // Get the current timestamp
        val timestamp = System.currentTimeMillis()

        // Prepare the order data
        val orderData = mutableMapOf<String, Any>()
        orderData["timestamp"] = timestamp
        orderData["totalPrice"] = order.getTotalPrice()

        val pizzasList = mutableListOf<Map<String, Any>>()
        for (pizza in order.getOrder()) {
            val pizzaData = mutableMapOf<String, Any>()
            pizzaData["size"] = pizza.getSizeStr()
            pizzaData["crust"] = pizza.getCrust()
            pizzaData["cheese"] = pizza.getCheese()
            pizzaData["toppings"] = pizza.getToppings()
            pizzasList.add(pizzaData)
        }
        orderData["pizzas"] = pizzasList

        newOrderRef.setValue(orderData)
            .addOnSuccessListener {
                // Order saved successfully
                Toast.makeText(this, "Order saved to account", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                // Handle failure
                Toast.makeText(this, "Failed to save order: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }



    private fun displayorder(order: Order) {
        val totalPrice = order.getTotalPrice()

        var summary = order.orderSummary()
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

    private fun saveCreditInfo(name: String, number: String, expire: String, cvv: String) {
        val sharedPref = getSharedPreferences("CreditInfo", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("CardName", name)
            putString("CardNumber", number)
            putString("CardExpiry", expire)
            putString("CardCVV", cvv)
            apply()
        }
    }

    private fun displayCreditInfo() {
        val sharedPref = getSharedPreferences("CreditInfo", Context.MODE_PRIVATE)
        val name = sharedPref.getString("CardName", "")
        val number = sharedPref.getString("CardNumber", "")
        val expiry = sharedPref.getString("CardExpiry", "")
        val cvv = sharedPref.getString("CardCVV", "")
        // Display the saved credit info
        cardname.setText(name)
        cardnumber.setText(number)
        cardexpire.setText(expiry)
        cardcvv.setText(cvv)
    }

    fun sendEmail(order: Order, recipient: String) {
        val subject = "Piazza Hut"
        val body = "Thank you for ordering! Your order is being prepared.\n" + order.orderSummary()

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

}

