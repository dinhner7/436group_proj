package com.example.a436group_proj

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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


        //Retrieves username to save order or redirects to login if not logged in
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", null)
        if (username == null) {
            Toast.makeText(this, "User username not found. Please log in again.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        val order = HomeActivity.order
        displayorder(order)
        displayCreditInfo()

        val hasDiscount = checkForDiscount()
        if (hasDiscount) {
            order.applyDiscount(10) // 할인을 실제로 적용
            discountInfoTV.text = "Discount applied: 10%"
            totalPriceTV.text = "Total Price: $${"%.2f".format(order.getTotalPrice())}"
        }

        rating.setOnRatingBarChangeListener { _, rating, _ ->
            if(rating>0){
                Toast.makeText(this, "Thank you for rating our small business!",
                    Toast.LENGTH_SHORT).show()
            }
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
                if(rating.rating==5f){
                    saveRating(true)
                    Toast.makeText(this,"Thank you for the 5-star rating! A 10% discount will be applied to your next order.",Toast.LENGTH_SHORT).show()
                } else {
                    saveRating(false)
                }
                saveCreditInfo(cardNameString, cardNumberString, cardExpireString, cardCVVString)

                // Save the order to Firebase
                if (username != null) {
                    saveOrderToFirebase(order, username)
                } else {
                    Toast.makeText(this, "User username not found", Toast.LENGTH_SHORT).show()
                }
                
                HomeActivity.order.clearOrder()
                Toast.makeText(this, "Thank you for ordering from Piazza Hut", Toast.LENGTH_SHORT).show()


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

    private fun saveRating(hasDiscount: Boolean) {
        val sharedPref = getSharedPreferences("RatingData", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("hasGivenFiveStars", hasDiscount)
            apply()
        }

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", null)
        if (username != null) {
            saveRatingToFirebase(username, hasDiscount)
        }
    }

    private fun saveRatingToFirebase(username: String, hasDiscount: Boolean) {
        val safeUsername = username.replace(".", ",")
        val userRef = database.child(safeUsername)

        userRef.child("hasGivenFiveStars").setValue(hasDiscount)
            .addOnSuccessListener {
                Log.d("Firebase", "Rating successfully saved to Firebase.")
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Failed to save rating to Firebase: ${exception.message}")
            }
    }

    private fun displayorder(order: Order) {
        val totalPrice = order.getTotalPrice()

        var summary = order.orderSummary()
        itemsTV.text = summary.toString()


        totalPriceTV.text = "Total Price: $${"%.2f".format(totalPrice)}"
    }


    private fun checkForDiscount(): Boolean {
        val sharedPref = getSharedPreferences("RatingData", Context.MODE_PRIVATE)
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", null)

        if (username != null) {
            val safeUsername = username.replace(".", ",")
            val userRef = database.child(safeUsername)

            userRef.child("hasGivenFiveStars").get().addOnSuccessListener { snapshot ->
                val hasGivenFiveStars = snapshot.getValue(Boolean::class.java) ?: false
                with(sharedPref.edit()) {
                    putBoolean("hasGivenFiveStars", hasGivenFiveStars)
                    apply()
                }
            }.addOnFailureListener { exception ->
                Log.e("Firebase", "Failed to fetch discount info: ${exception.message}")
            }
        }


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

}

