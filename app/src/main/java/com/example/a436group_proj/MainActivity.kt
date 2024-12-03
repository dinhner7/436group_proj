package com.example.a436group_proj

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var createAccountButton: Button
    private lateinit var nameInput: EditText
    private lateinit var nameLabel: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        autoLogin()


        // Initialize UI elements
        usernameInput = findViewById(R.id.usernameInputET)
        passwordInput = findViewById(R.id.passwordInputET)
        loginButton = findViewById(R.id.loginButton)
        createAccountButton = findViewById(R.id.createAccountButton)
        nameInput = findViewById(R.id.nameInputET)
        nameLabel = findViewById(R.id.nameLabelTXT)


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


                if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
                    createAccount(email, password, name)
                } else {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    private fun showAccountCreationFields() {
        nameInput.visibility = View.VISIBLE
        nameLabel.visibility = View.VISIBLE
        loginButton.visibility = View.GONE
        Toast.makeText(this, "Enter your name and address to complete account creation", Toast.LENGTH_SHORT).show()
    }

    private fun hideAccountCreationFields() {
        nameInput.visibility = View.GONE
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
                    saveLoginState(email)
                    Toast.makeText(this@MainActivity, "Login successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@MainActivity, "Invalid email or password", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Database error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun createAccount(email: String, password: String, name: String) {
        val safeEmail = email.replace(".", ",")
        val userMap = mapOf(
            "accountInfo" to mapOf(
                "email" to email,
                "password" to password,
                "name" to name,
                "address" to mapOf(
                    "street" to "",
                    "city" to "",
                    "state" to "",
                    "zip" to ""
                )
            ),
            "orders" to "" // Placeholder for orders
        )
        database.child(safeEmail).setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Account created successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, Customize::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@MainActivity, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun saveLoginState(email: String) {
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.apply()
    }


    private fun autoLogin() {
        val email = sharedPreferences.getString("email", null)
        if (email != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}