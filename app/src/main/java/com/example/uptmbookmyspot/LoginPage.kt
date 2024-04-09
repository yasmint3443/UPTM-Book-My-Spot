package com.example.uptmbookmyspot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.uptmbookmyspot.Model.Model
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginPage : AppCompatActivity() {

    // initialize the component
    private lateinit var btnLog : Button
    private lateinit var email : EditText
    private lateinit var password : EditText

    // declare the firebase
    private lateinit var firebaseDatabase : FirebaseDatabase
    private lateinit var databaseReference : DatabaseReference

    // declare the firebase authentication
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        //declare the component
        btnLog = findViewById<Button>(R.id.btnLogin)
        email = findViewById<EditText>(R.id.emailLogin)
        password = findViewById<EditText>(R.id.passLogin)

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("User")

        btnLog.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty())
            {
                logIn(email, password)
            } else {
                Toast.makeText(this@LoginPage, "All fields are mandatory", Toast.LENGTH_LONG).show()
            }
        }
    }

    // create the function logIn
    // this function read data to firebase
    // p = password
    // e = email

    private fun logIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        // Check if the logged-in user is an admin
                        if (user.email == "admin@gmail.com") {
                            // Redirect to adminDashboard
                            Toast.makeText(this@LoginPage, "Login Successful", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this@LoginPage, adminDashboard::class.java))
                        } else {
                            // Redirect to homepage for regular users
                            Toast.makeText(this@LoginPage, "Login Successful", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this@LoginPage, Homepage::class.java))
                        }
                        finish()
                    } else {
                        Toast.makeText(this@LoginPage, "Failed to get current user", Toast.LENGTH_LONG).show()
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@LoginPage, "Login Failed", Toast.LENGTH_LONG).show()
                }
            }
    }
}