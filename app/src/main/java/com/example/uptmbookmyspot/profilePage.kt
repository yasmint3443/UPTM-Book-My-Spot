package com.example.uptmbookmyspot

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.example.uptmbookmyspot.Model.Model
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.*

class profilePage : AppCompatActivity() {

    // INITIALIZE
    // Firebase Authentication
    private lateinit var auth: FirebaseAuth

    // TextViews
    private lateinit var nameTextView: TextView
    private lateinit var idNumberTextView: TextView
    private lateinit var phoneNumberTextView: TextView
    private lateinit var emailTextView: TextView

    // other component
    private lateinit var manual : CardView
    private lateinit var faq : CardView
    private lateinit var logout : ImageView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        // declare Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // declare TextViews
        nameTextView = findViewById(R.id.nameTextView)
        idNumberTextView = findViewById(R.id.idNumberTextView)
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView)
        emailTextView = findViewById(R.id.emailTextView)

        // declare other component
        manual = findViewById(R.id.userManualCardView)
        faq = findViewById(R.id.faqCardView)
        logout = findViewById(R.id.logoutButton)

        manual.setOnClickListener {
            val intent = Intent(this, UserManual::class.java)
            startActivity(intent)
        }

        // Set click listener for the FAQ card
        faq.setOnClickListener {
            val intent = Intent(this, Faq::class.java)
            startActivity(intent)
        }

        // Set click listener for the logout button
        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut() // Log out the user
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear the activity stack
            startActivity(intent)
        }

        // Ensure there is a logged-in user
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "No authenticated user found.", Toast.LENGTH_SHORT).show()
            finish() // Close the activity or redirect the user to the login screen
            return
        }

        // Retrieve current user
        val userId = currentUser.uid

        // Access the user's information from the Realtime Database
        val userRef = FirebaseDatabase.getInstance().getReference("User").child(userId)
        Log.d("profilePage", "Fetching data for user ID: $userId")
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(Model::class.java)
                user?.let {
                    nameTextView.text = "Name: ${it.userName}"
                    idNumberTextView.text = "ID Number: ${it.userIdNum}"
                    phoneNumberTextView.text = "Phone Number: ${it.userPhone}"
                    emailTextView.text = "Email: ${currentUser.email}" // Email directly from Auth
                } ?: run {
                    Toast.makeText(
                        applicationContext,
                        "User details not found.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("profilePage", "Failed to load user details", error.toException())
                Toast.makeText(
                    applicationContext,
                    "Failed to load user details.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
