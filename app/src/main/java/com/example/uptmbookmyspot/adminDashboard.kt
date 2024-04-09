package com.example.uptmbookmyspot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth

class adminDashboard : AppCompatActivity() {

    private lateinit var newsCard: CardView
    //private lateinit var facCard: CardView
    private lateinit var logout : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        newsCard = findViewById<CardView>(R.id.newsCard)
        //facCard = findViewById<CardView>(R.id.facCard)
        logout = findViewById(R.id.logoutButton)

        newsCard.setOnClickListener {
            val intent = Intent(this, AdminNews::class.java)
            startActivity(intent)
        }

        /*facCard.setOnClickListener {
            val intent = Intent(this, adminfacility::class.java)
            startActivity(intent)
        }*/

        // Set click listener for the logout button
        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut() // Log out the user
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear the activity stack
            startActivity(intent)
        }
    }
}