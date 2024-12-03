package com.example.a436group_proj

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var createAccountButton: Button
    private lateinit var nameInput: EditText
    private lateinit var streetInput: EditText
    private lateinit var cityInput: EditText
    private lateinit var stateInput: EditText
    private lateinit var zipInput: EditText
    private lateinit var nameLabel: TextView
    private lateinit var addressLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Realtime Database
        FirebaseApp.initializeApp(this)
        if (FirebaseApp.getApps(this).isNotEmpty()) {
            Log.d("FirebaseInit", "Firebase initialized successfully")
        } else {
            Log.e("FirebaseInit", "Firebase initialization failed")
        }
        database = FirebaseDatabase.getInstance().reference.child("users")

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // Check if user is already logged in
        if (isUserLoggedIn()) {
            autoLogin()
        }

        // Initialize UI elements
        usernameInput = findViewById(R.id.usernameInputET)
        passwordInput = findViewById(R.id.passwordInputET)
        loginButton = findViewById(R.id.loginButton)
        createAccountButton = findViewById(R.id.createAccountButton)
        nameInput = findViewById(R.id.nameInputET)
        streetInput = findViewById(R.id.streetInputET)
        cityInput = findViewById(R.id.cityInputET)
        stateInput = findViewById(R.id.stateInputET)
        zipInput = findViewById(R.id.zipInputET)
        nameLabel = findViewById(R.id.nameLabelTXT)
        addressLabel = findViewById(R.id.addressLabelTXT)

        Log.d("Debug", "CreateAccountButton initialized: $createAccountButton")

        // Handle Login
        loginButton.setOnClickListener {
            val email = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle Account Creation
        createAccountButton.setOnClickListener {
            if (nameInput.visibility == View.GONE) {
                showAccountCreationFields()
            } else {
                val email = usernameInput.text.toString().trim()
                val password = passwordInput.text.toString()
                val name = nameInput.text.toString().trim()
                val street = streetInput.text.toString().trim()
                val city = cityInput.text.toString().trim()
                val state = stateInput.text.toString().trim()
                val zip = zipInput.text.toString().trim()

                if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && street.isNotEmpty() && city.isNotEmpty() && state.isNotEmpty() && zip.isNotEmpty()) {
                    createAccount(email, password, name, street, city, state, zip)
                } else {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showAccountCreationFields() {
        nameInput.visibility = View.VISIBLE
        streetInput.visibility = View.VISIBLE
        cityInput.visibility = View.VISIBLE
        stateInput.visibility = View.VISIBLE
        zipInput.visibility = View.VISIBLE
        addressLabel.visibility = View.VISIBLE
        nameLabel.visibility = View.VISIBLE
        loginButton.visibility = View.GONE
        Toast.makeText(this, "Enter your name and address to complete account creation", Toast.LENGTH_SHORT).show()
    }

    private fun hideAccountCreationFields() {
        nameInput.visibility = View.GONE
        streetInput.visibility = View.GONE
        cityInput.visibility = View.GONE
        stateInput.visibility = View.GONE
        zipInput.visibility = View.GONE
        addressLabel.visibility = View.GONE
        nameLabel.visibility = View.GONE
        loginButton.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        if (nameInput.visibility == View.VISIBLE) {
            hideAccountCreationFields()
        } else {
            super.onBackPressed()
        }
    }

    private fun loginUser(email: String, password: String) {
        val safeEmail = email.replace(".", ",")
        database.child(safeEmail).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val storedPassword = snapshot.child("accountInfo/password").getValue(String::class.java)
                if (storedPassword != null && storedPassword == password) {
                    saveLoginState(email, true)
                    Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Invalid email or password", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LoginActivity, "Database error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun createAccount(email: String, password: String, name: String, street: String, city: String, state: String, zip: String) {
        val safeEmail = email.replace(".", ",")
        val userMap = mapOf(
            "accountInfo" to mapOf(
                "email" to email,
                "password" to password,
                "name" to name,
                "address" to mapOf(
                    "street" to street,
                    "city" to city,
                    "state" to state,
                    "zip" to zip
                )
            ),
            "orders" to "" // Placeholder for orders
        )
        database.child(safeEmail).setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveLoginState(email, true)
                    Toast.makeText(this@LoginActivity, "Account created successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, Customize::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun saveLoginState(email: String, isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.apply()
    }

    private fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    private fun autoLogin() {
        val email = sharedPreferences.getString("email", null)
        if (email != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun logout() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
    }
}
