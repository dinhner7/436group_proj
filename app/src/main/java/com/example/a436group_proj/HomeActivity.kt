package com.example.a436group_proj

import android.content.Intent
import android.content.SharedPreferences
import com.google.android.gms.ads.AdRequest
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import kotlin.math.log
import android.view.LayoutInflater
import android.widget.LinearLayout
import java.text.SimpleDateFormat
import java.util.Locale
import android.app.AlertDialog





class HomeActivity : AppCompatActivity() {
    private lateinit var greetingTV : TextView
    private lateinit var logoutB : AppCompatButton
    private lateinit var addCheesePizzaB : AppCompatButton
    private lateinit var addPepperoniPizzaB : AppCompatButton
    private lateinit var customizePizzaB : AppCompatButton
    private lateinit var checkoutB : AppCompatButton
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var lastOrderTV : TextView

    private lateinit var ad : InterstitialAd
    private lateinit var database: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = FirebaseDatabase.getInstance() // .reference.child("users")


        setContentView(R.layout.activity_home)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        order = Order()

        // change name in greeting
        greetingTV = findViewById(R.id.greeting)
        val email = intent.getStringExtra("USERNAME")
        greetingTV.text = "Hello, " + email + "!"
        logoutB = findViewById(R.id.logout)
        addCheesePizzaB = findViewById(R.id.addCheesePizza)
        addPepperoniPizzaB = findViewById(R.id.addPepperoniPizza)
        customizePizzaB = findViewById(R.id.sendToCustomize)
        checkoutB = findViewById(R.id.checkout)

        var builder : AdRequest.Builder = AdRequest.Builder()
        builder.addKeyword("food")
        var request : AdRequest = builder.build()

        var adUnitId : String = "ca-app-pub-3940256099942544/1033173712"
        var adLoad : AdLoad = AdLoad()


        //Past Orders

        val ordersLayout = findViewById<LinearLayout>(R.id.ordersLinearLayout)

        // last order line 95

        logoutB.setOnClickListener { logout() }
        addCheesePizzaB.setOnClickListener { addCheesePizza() }
        addPepperoniPizzaB.setOnClickListener { addPepperoniPizza() }
        customizePizzaB.setOnClickListener { sendToCustomize() }
        checkoutB.setOnClickListener { sendToCheckout(request, adUnitId, adLoad) }

        val ordersRef = database.getReference("users").child(email!!).child("orders")
        ordersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val ordersList = mutableListOf<Map<String, Any>>()
                    dataSnapshot.children.forEach { orderSnapshot ->
                        val order = orderSnapshot.getValue(object :
                            GenericTypeIndicator<Map<String, Any>>() {})
                        if (order != null) {
                            ordersList.add(order)
                        }
                    }
                    // Iterate over past orders and create cards
                    for (orderData in ordersList) {
                        // Inflate the order_card layout
                        val orderView = LayoutInflater.from(this@HomeActivity).inflate(R.layout.order_card, ordersLayout, false)

                        //make orders clickable
                        orderView.setOnClickListener {
                            showOrderDetailsDialog(orderData)
                        }


                        // Set Order Date
                        val orderDateTV = orderView.findViewById<TextView>(R.id.orderDateTV)
                        val timestamp = orderData["timestamp"] as? Long ?: System.currentTimeMillis()
                        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                        val dateString = dateFormat.format(timestamp)
                        orderDateTV.text = "$dateString"

                        // Set Total Price
                        val totalPriceTV = orderView.findViewById<TextView>(R.id.totalPriceTV)
                        val totalPrice = orderData["totalPrice"] as? Double ?: 0.0
                        totalPriceTV.text = "$${String.format("%.2f", totalPrice)}"

                        // Get Pizza Details
                        val pizzaDetailsLayout = orderView.findViewById<LinearLayout>(R.id.pizzaDetailsLayout)
                        val pizzas = orderData["pizzas"] as? List<Map<String, Any>> ?: emptyList()

                        for ((index, pizzaData) in pizzas.withIndex()) {
                            val pizzaSummaryTV = TextView(this@HomeActivity)
                            pizzaSummaryTV.textSize = 14f
                            pizzaSummaryTV.setTextColor(resources.getColor(R.color.white))
                            val size = pizzaData["size"] as? String ?: "Unknown Size"
                            val crust = pizzaData["crust"] as? String ?: "Unknown Crust"
                            val toppings = pizzaData["toppings"] as? List<String> ?: emptyList()
                            val toppingsString = if (toppings.isNotEmpty()) toppings.joinToString(", ") else "None"

                            pizzaSummaryTV.text = "Pizza ${index + 1}: $size, $crust crust, Toppings: $toppingsString"
                            pizzaDetailsLayout.addView(pizzaSummaryTV)
                        }

                        // Add the order card to the layout
                        ordersLayout.addView(orderView)
                    }
                } else {
                    // No past orders
                    val noOrdersTV = TextView(this@HomeActivity)
                    noOrdersTV.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    noOrdersTV.text = "No past orders"
                    noOrdersTV.textSize = 16f
                    noOrdersTV.setTextColor(resources.getColor(R.color.piazza_blue))
                    ordersLayout.addView(noOrdersTV)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error: ${error.message}")
            }
        })

    }

    // Log out and go back to login view
    fun logout() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
        // Redirect to the login screen (MainActivity)

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        finish()
    }

    // Add cheese pizza to the order
    fun addCheesePizza() {
        var pizza = Pizza()
        pizza.setSize("2")
        pizza.setCrust("normal")
        pizza.setCheese("mozzarella")
        order.addPizza(pizza)
        order.addToPrice(pizza.getTotalCost())

        Toast.makeText(this, "Classic Cheese added to cart", Toast.LENGTH_SHORT).show()
    }

    // Add pepperoni pizza to the order
    fun addPepperoniPizza() {
        var pizza = Pizza()
        pizza.setSize("2")
        pizza.setCrust("normal")
        pizza.setCheese("mozzarella")
        pizza.addTopping("pepperoni")
        order.addPizza(pizza)
        order.addToPrice(pizza.getTotalCost())

        Toast.makeText(this, "Pepperoni Delight added to cart", Toast.LENGTH_SHORT).show()
    }

    private fun showOrderDetailsDialog(orderData: Map<String, Any>) {
        // Create a dialog
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_order_details, null)
        dialogBuilder.setView(dialogView)

        // Initialize UI elements in the dialog
        val orderDetailsTV = dialogView.findViewById<TextView>(R.id.orderDetailsTV)
        val addToCartButton = dialogView.findViewById<AppCompatButton>(R.id.addToCartButton)

        // Set order details
        val pizzas = orderData["pizzas"] as? List<Map<String, Any>> ?: emptyList()
        val totalPrice = orderData["totalPrice"] as? Double ?: 0.0

        val summary = StringBuilder()
        pizzas.forEachIndexed { index, pizzaData ->
            summary.append("Pizza ${index + 1}:\n")
            val size = pizzaData["size"] as? String ?: "Unknown Size"
            val crust = pizzaData["crust"] as? String ?: "Unknown Crust"
            val cheese = pizzaData["cheese"] as? String ?: "Unknown Cheese"
            val toppings = pizzaData["toppings"] as? List<String> ?: emptyList()
            val toppingsString = if (toppings.isNotEmpty()) toppings.joinToString(", ") else "No Toppings"

            summary.append("Size: $size\n")
            summary.append("Crust: $crust\n")
            summary.append("Cheese: $cheese\n")
            summary.append("Toppings: $toppingsString\n\n")
        }
        summary.append("Total Price: $${String.format("%.2f", totalPrice)}")

        orderDetailsTV.text = summary.toString()

        // Create and show the dialog
        val alertDialog = dialogBuilder.create()
        alertDialog.show()

        // Handle the Add to Cart button
        addToCartButton.setOnClickListener {
            // Add the order to the current cart
            addOrderToCart(orderData)
            Toast.makeText(this, "Order added to cart", Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
        }
    }

    private fun addOrderToCart(orderData: Map<String, Any>) {
        val pizzas = orderData["pizzas"] as? List<Map<String, Any>> ?: emptyList()

        for (pizzaData in pizzas) {
            val pizza = Pizza()
            val size = pizzaData["size"] as? String ?: ""
            val crust = pizzaData["crust"] as? String ?: ""
            val cheese = pizzaData["cheese"] as? String ?: ""
            val toppings = pizzaData["toppings"] as? List<String> ?: emptyList()

            // Set size and base price
            when (size) {
                "Small" -> {
                    pizza.setSize("Small")
                    pizza.setBasePrice(10.00f)
                }
                "Medium" -> {
                    pizza.setSize("Medium")
                    pizza.setBasePrice(12.00f)
                }
                "Large" -> {
                    pizza.setSize("Large")
                    pizza.setBasePrice(16.00f)
                }
                else -> {
                    pizza.setSize("Unknown")
                    pizza.setBasePrice(0f)
                }
            }

            // Set crust and cheese
            pizza.setCrust(crust)
            pizza.setCheese(cheese)

            // Add toppings
            for (topping in toppings) {
                pizza.addTopping(topping)
            }

            // Add pizza to the current order
            order.addPizza(pizza)
            order.addToPrice(pizza.getTotalCost())
        }
    }


    // Direct to customization view
    fun sendToCustomize() {
        var intent : Intent = Intent( this, Customize::class.java )
        this.startActivity( intent )
    }

    // Direct to checkout view
    fun sendToCheckout(request : AdRequest, adUnitId : String, adLoad : AdLoad) {

        InterstitialAd.load(this, adUnitId, request, adLoad)
    }

    inner class AdLoad : InterstitialAdLoadCallback() {
        override fun onAdFailedToLoad(p0: LoadAdError) {
            super.onAdFailedToLoad(p0)
        }

        override fun onAdLoaded(p0: InterstitialAd) {
            super.onAdLoaded(p0)

            ad = p0
            ad.show(this@HomeActivity)

            var manageAdShowing: ManageAdShowing = ManageAdShowing()
            ad.fullScreenContentCallback = manageAdShowing
        }


    }

    inner class ManageAdShowing : FullScreenContentCallback() {
        override fun onAdClicked() {
            super.onAdClicked()
        }

        override fun onAdImpression() {
            super.onAdImpression()
        }

        override fun onAdShowedFullScreenContent() {
            super.onAdShowedFullScreenContent()
        }

        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
            super.onAdFailedToShowFullScreenContent(p0)
        }

        override fun onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent()

            // go to next view
            var intent : Intent = Intent( this@HomeActivity, ConfirmActivity::class.java )
            this@HomeActivity.startActivity( intent )
        }
    }

    companion object{
        lateinit var order: Order
    }
}