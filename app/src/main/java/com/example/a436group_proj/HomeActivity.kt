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
        lastOrderTV = findViewById(R.id.last_order)

        var builder : AdRequest.Builder = AdRequest.Builder()
        builder.addKeyword("food")
        var request : AdRequest = builder.build()

        var adUnitId : String = "ca-app-pub-3940256099942544/1033173712"
        var adLoad : AdLoad = AdLoad()

        // last order line 95

        logoutB.setOnClickListener { logout() }
        addCheesePizzaB.setOnClickListener { addCheesePizza() }
        addPepperoniPizzaB.setOnClickListener { addPepperoniPizza() }
        customizePizzaB.setOnClickListener { sendToCustomize() }
        checkoutB.setOnClickListener { sendToCheckout(request, adUnitId, adLoad) }

        val ordersRef = database.getReference("users").child(email!!).child("accountInfo").child("orders")
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
                    // Use the retrieved orders list
                    val summary = StringBuilder()
                    ordersList.forEachIndexed { index, pizza ->
                        summary.append("Pizza ${index + 1}:\n")
                        summary.append(pizza)
                    }

                    lastOrderTV.text = summary.toString()

                } else {
                    //Log.d("Firebase", "No orders found")
                    lastOrderTV.text = "No past orders"
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