package com.example.uptmbookmyspot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Homepage : AppCompatActivity() {

    private lateinit var newsCard: CardView
    private lateinit var facCard: CardView
    private lateinit var calendarCard: CardView
    private lateinit var contactCard: CardView
    private lateinit var linkCard: CardView
    private lateinit var profileCard: CardView
    private lateinit var displayName : TextView

    // Firebase Authentication
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        newsCard = findViewById(R.id.newsCard)
        facCard = findViewById(R.id.facCard)
        calendarCard = findViewById(R.id.calendarCard)
        contactCard = findViewById(R.id.contactCard)
        linkCard = findViewById(R.id.linkCard)
        profileCard = findViewById(R.id.profileCard)
        displayName = findViewById(R.id.userName)

        // declare Firebase Authentication
        auth = FirebaseAuth.getInstance()

        fetchUserName()

        newsCard.setOnClickListener {
            val intent = Intent(this@Homepage, News::class.java)
            startActivity(intent)
        }

        facCard.setOnClickListener {
            val intent = Intent(this@Homepage, MyBookings::class.java)
            startActivity(intent)
        }

        calendarCard.setOnClickListener {
            val intent = Intent(this@Homepage, Calendar::class.java)
            startActivity(intent)
        }

        contactCard.setOnClickListener {
            val intent = Intent(this@Homepage, contactUs::class.java)
            startActivity(intent)
        }

        linkCard.setOnClickListener {
            val intent = Intent(this@Homepage, quickLinks::class.java)
            startActivity(intent)
        }

        profileCard.setOnClickListener {
            val intent = Intent(this@Homepage, profilePage::class.java)
            startActivity(intent)
        }
    }

    private fun fetchUserName() {
        // Ensure there is a logged-in user
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Retrieve current user ID
            val userId = currentUser.uid

            // Access the user's information from the Realtime Database
            val userRef = FirebaseDatabase.getInstance().getReference("User").child(userId)
            Log.d("HomePage", "Fetching name for user ID: $userId")
            userRef.child("userName").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Directly fetch the userName field from the snapshot
                    val userName = snapshot.getValue(String::class.java)
                    userName?.let {
                        // Update the UI to show the user's name
                        displayName.text = "$it"
                    } ?: run {
                        Toast.makeText(applicationContext, "User name not found.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("HomePage", "Failed to load user name", error.toException())
                    Toast.makeText(applicationContext, "Failed to load user name.", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "No authenticated user found.", Toast.LENGTH_SHORT).show()
        }
    }
}