package com.example.a436group_proj

import android.content.Intent
import com.google.android.gms.ads.AdRequest
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlin.math.log

class HomeActivity : AppCompatActivity() {
    private lateinit var greetingTV : TextView
    private lateinit var logoutB : AppCompatButton
    private lateinit var addCheesePizzaB : AppCompatButton
    private lateinit var addPepperoniPizzaB : AppCompatButton
    private lateinit var customizePizzaB : AppCompatButton
    private lateinit var checkoutB : AppCompatButton

    private lateinit var ad : InterstitialAd


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        order = Order()

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
        var builder : AdRequest.Builder = AdRequest.Builder()
        builder.addKeyword("food")
        var request : AdRequest = builder.build()

        var adUnitId : String = "ca-app-pub-3940256099942544/1033173712"
        var adLoad : AdLoad = AdLoad()
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
            var intent : Intent = Intent( this@HomeActivity, CheckoutActivity::class.java )
            this@HomeActivity.startActivity( intent )
        }
    }

    companion object{
        lateinit var order: Order
    }
}