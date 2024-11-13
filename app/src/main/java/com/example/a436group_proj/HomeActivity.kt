package com.example.a436group_proj

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import kotlin.math.log

class HomeActivity : AppCompatActivity() {
    private lateinit var greetingTV : TextView
    private lateinit var logoutB : AppCompatButton
    private lateinit var addCheesePizzaB : AppCompatButton
    private lateinit var addPepperoniPizzaB : AppCompatButton
    private lateinit var customizePizzaB : AppCompatButton
    private lateinit var checkoutB : AppCompatButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        greetingTV = findViewById(R.id.greeting)
        logoutB = findViewById(R.id.logout)
        addCheesePizzaB = findViewById(R.id.addCheesePizza)
        addPepperoniPizzaB = findViewById(R.id.addPepperoniPizza)
        customizePizzaB = findViewById(R.id.sendToCustomize)
        checkoutB = findViewById(R.id.checkout)

        logoutB.setOnClickListener { logout() }
        addCheesePizzaB.setOnClickListener { addCheesePizza() }
        addPepperoniPizzaB.setOnClickListener { addPepperoniPizza() }
        customizePizzaB.setOnClickListener { sendToCustomize() }
        checkoutB.setOnClickListener { sendToCheckout() }


    }

    // Log out and go back to login view
    fun logout() {
        finish()
    }

    // Add cheese pizza to the order
    fun addCheesePizza() {

    }

    // Add pepperoni pizza to the order
    fun addPepperoniPizza() {

    }

    // Direct to customization view
    fun sendToCustomize() {
        var intent : Intent = Intent( this, Customize::class.java )
        this.startActivity( intent )
    }

    // Direct to checkout view
    fun sendToCheckout() {

    }
}