package com.example.uptmbookmyspot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker

class facilityDate : AppCompatActivity() {

    private lateinit var datePicker: DatePicker
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facility_date)

        datePicker = findViewById(R.id.date_picker)
        nextButton = findViewById(R.id.next_button)

        // Set onClickListener for Next button
        nextButton.setOnClickListener {
            // Handle Next button click
            // Navigate to TimeActivity or perform any other action
        }
    }
}