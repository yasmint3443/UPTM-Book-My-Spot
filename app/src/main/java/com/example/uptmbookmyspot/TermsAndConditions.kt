package com.example.uptmbookmyspot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class TermsAndConditions : AppCompatActivity() {

    private lateinit var closeBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_conditions)

        closeBtn = findViewById<Button>(R.id.closeButton)

        closeBtn.setOnClickListener {
            // Simply close this activity. If BookingSummaryActivity is below this in the stack, it will be shown.
            finish()
        }
    }
}