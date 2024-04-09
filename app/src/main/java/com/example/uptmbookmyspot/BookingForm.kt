package com.example.uptmbookmyspot

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uptmbookmyspot.Model.BookingDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class BookingForm : AppCompatActivity() {
    // Declare all components
    private lateinit var pickDateBtn: Button
    private lateinit var dateTV: TextView
    private lateinit var checkBox: CheckBox
    private lateinit var confirmBtn: Button
    private lateinit var timeSlotSpinner: Spinner
    private lateinit var termsTV: TextView
    private lateinit var name: EditText
    private lateinit var phone: EditText
    private lateinit var pax: EditText

    // Declare to connect with the database
    private lateinit var dbRef: DatabaseReference

    // Declare a list to store available time slots
    private val availableTimeSlots = mutableListOf<String>()

    // Declare a list to store booked time slots
    private val bookedTimeSlots = mutableListOf<String>()

    // Declare a spinner adapter
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_form)

        // Initialize UI elements
        pickDateBtn = findViewById<Button>(R.id.pickDateBtn)
        dateTV = findViewById<TextView>(R.id.dateTV)
        checkBox = findViewById<CheckBox>(R.id.agreeCB)
        confirmBtn = findViewById<Button>(R.id.btnConfirm)
        timeSlotSpinner = findViewById<Spinner>(R.id.timeSlotSpinner)
        termsTV = findViewById<TextView>(R.id.termsTV)
        name = findViewById<EditText>(R.id.nameET)
        phone = findViewById<EditText>(R.id.phoneET)
        pax = findViewById<EditText>(R.id.paxET)

        // Initialize spinner adapter
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, availableTimeSlots)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        timeSlotSpinner.adapter = adapter

        // Fetch booked time slots from Firebase for the specified facility
        val facilityName = intent.getStringExtra("FACILITY_NAME") ?: "Default Facility Name"
        fetchBookedTimeSlotsFromFirebase(facilityName)

        // Set click listeners
        setClickListeners()

        // Initialize available time slots
        initializeAvailableTimeSlots()

        // Initialize calendar
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Set pickDateBtn click listener
        pickDateBtn.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view: DatePicker?, mYear: Int, mMonth: Int, mDay: Int ->
                    // Correct the month value by adding 1
                    val adjustedMonth = mMonth + 1
                    // set to textview
                    val formattedDate = ("" + mDay + "/" + adjustedMonth + "/" + mYear)
                    dateTV.text = formattedDate
                },
                year,
                month,
                day
            )
            // Show dialog
            dpd.show()
        }
    }

    // Function to set click listeners
    private fun setClickListeners() {
        confirmBtn.setOnClickListener {
            if (!checkBox.isChecked) {
                // Show a dialog reminding the user to agree to the T&C
                AlertDialog.Builder(this)
                    .setTitle("Terms and Conditions")
                    .setMessage("You must agree to the terms and conditions before proceeding.")
                    .setPositiveButton("Agree") { dialog, which ->
                        // User agrees through the dialog, so check the checkbox and proceed if needed
                        checkBox.isChecked = true
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            } else {
                // If T&C are agreed, proceed directly
                // Use dateTV for the date, and ensure you get the correct types and values
                val date = dateTV.text.toString()
                val facilityName = intent.getStringExtra("FACILITY_NAME") ?: "Default Facility Name"
                val name = name.text.toString()
                val pax = pax.text.toString()
                val timeSlot = timeSlotSpinner.selectedItem.toString()
                val phoneNumber = phone.text.toString()
                saveBooking(date, facilityName, name, pax, phoneNumber, timeSlot)
            }
        }
        // Click to see the T&C
        termsTV.setOnClickListener {
            val intent = Intent(this, TermsAndConditions::class.java)
            startActivity(intent)
        }
    }

    // Function to fetch booked time slots from Firebase for a specific facility
    private fun fetchBookedTimeSlotsFromFirebase(facilityName: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Bookings")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bookedTimeSlots.clear()
                // Loop through all bookings
                for (bookingSnapshot in snapshot.children) {
                    val booking = bookingSnapshot.getValue(BookingDetails::class.java)
                    booking?.let {
                        // Filter bookings for the specified facility
                        if (it.facilityName == facilityName) {
                            // Add booked time slot to the list
                            it.timeSlot?.let { it1 -> bookedTimeSlots.add(it1) }
                        }
                    }
                }
                // Remove booked time slots from the list of available time slots
                availableTimeSlots.removeAll(bookedTimeSlots)
                // Update the spinner adapter with the updated list of available time slots
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event
                // Log the error
                Log.e("BookingForm", "Failed to fetch booked time slots: ${error.message}")

                // Inform the user that fetching data failed
                Toast.makeText(
                    applicationContext,
                    "Failed to fetch booked time slots: ${error.details}",
                    Toast.LENGTH_LONG
                ).show()
                confirmBtn.isEnabled = false // Example: Disable the confirm button
            }
        })
    }

    // Function to save booking data to Firebase
    private fun saveBooking(d: String, f: String, n: String, p: String, t: String, s: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        dbRef = FirebaseDatabase.getInstance().getReference("Bookings")

        // Produce auto generate booking ID
        val bookingId = dbRef.push().key!!

        try {
            // Save the booking information under the generated ID with alphabetical ordering
            dbRef.child(bookingId).apply {
                child("bookingId").setValue(bookingId)
                child("date").setValue(d)
                child("facilityName").setValue(f)
                child("name").setValue(n)
                child("pax").setValue(p)
                child("phoneNumber").setValue(t)
                child("timeSlot").setValue(s)
                child("userId").setValue(userId)
            }

            // Proceed to BookingSummary with the booking details
            val intent = Intent(this, BookingSummary::class.java).apply {
                putExtra("BOOKING_ID", bookingId)
                putExtra("DATE", d)
                putExtra("FACILITY_NAME", f)
                putExtra("NAME", n)
                putExtra("PAX", p)
                putExtra("PHONE_NUMBER", t)
                putExtra("TIME_SLOT", s)
                putExtra("USER_ID", userId)
            }
            startActivity(intent)

            Toast.makeText(
                applicationContext,
                "Booking saved successfully",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            Toast.makeText(
                applicationContext,
                "Failed to save booking: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
            Log.e("BookingForm", "Failed to save booking", e)
        }
    }

    // Function to initialize available time slots
    private fun initializeAvailableTimeSlots() {
        // Generate time slots from 8 AM to 6 PM
        for (hour in 8..11) {
            val timeSlot = "$hour:00 AM - ${hour + 1}:00 AM"
            availableTimeSlots.add(timeSlot)
        }
        availableTimeSlots.add("12:00 PM - 1:00 PM") // Noon slot
        for (hour in 1..5) {
            val timeSlot = "$hour:00 PM - ${hour + 1}:00 PM"
            availableTimeSlots.add(timeSlot)
        }
    }
}
