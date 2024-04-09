package com.example.uptmbookmyspot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    // initialize the component
    private lateinit var login: Button
    private lateinit var signup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // declare all the component
        login = findViewById<Button>(R.id.login)
        signup = findViewById<Button>(R.id.signup)

        // function to go login page
        login.setOnClickListener {
            val i = Intent(this, LoginPage::class.java)
            startActivity(i)
        }

        // function to go signup page
        signup.setOnClickListener {
            val a = Intent(this, RegisterPage::class.java)
            startActivity(a)
        }
    }
}