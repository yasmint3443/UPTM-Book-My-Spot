package com.example.uptmbookmyspot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.uptmbookmyspot.Model.BookingDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BookingSummary : AppCompatActivity() {

    // Declare all components
    private lateinit var dbRef: DatabaseReference
    private lateinit var btnH : Button
    private lateinit var btnC : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_summary)

        // Initialize components
        btnH = findViewById(R.id.btnHome)
        btnC = findViewById(R.id.btnCancel)

        // Redirect to homepage
        btnH.setOnClickListener {
            val intent = Intent(this@BookingSummary, Homepage::class.java)
            startActivity(intent)
        }

        // Button cancel booking
        btnC.setOnClickListener {
            // Retrieve the booking ID from the Intent extras or from the TextView
            val bookingId = intent.getStringExtra("BOOKING_ID") ?: return@setOnClickListener

            // Call the cancelBooking function with the booking ID
            cancelBooking(bookingId)
        }

        // Retrieve the booking details from the Intent extras
        val bookingId = intent.getStringExtra("BOOKING_ID")
        val date = intent.getStringExtra("DATE")
        val facilityName = intent.getStringExtra("FACILITY_NAME")
        val name = intent.getStringExtra("NAME")
        val pax = intent.getStringExtra("PAX")
        val phoneNumber = intent.getStringExtra("PHONE_NUMBER")
        val timeSlot = intent.getStringExtra("TIME_SLOT")
        val userId = intent.getStringExtra("USER_ID")

        if (bookingId != null && date != null && facilityName != null && name != null && pax != null && phoneNumber != null && timeSlot != null && userId != null) {
            // If all details are present, update the UI directly
            updateUIWithBookingDetails(BookingDetails(bookingId, date, facilityName, name, pax, phoneNumber, timeSlot, userId))
        } else if (bookingId != null) {
            // If only the booking ID is available, fetch the details from Firebase
            fetchBookingDetails(bookingId)
        } else {
            // If no data is available, show an error message
            Toast.makeText(this, "Error retrieving booking details.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchBookingDetails(bookingId: String) {
        dbRef = FirebaseDatabase.getInstance().getReference("Bookings")
        dbRef.child(bookingId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val booking = snapshot.getValue(BookingDetails::class.java)
                if (booking != null) {
                    updateUIWithBookingDetails(booking)
                } else {
                    Toast.makeText(applicationContext, "Booking details not found.", Toast.LENGTH_LONG).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("BookingSummary", "DatabaseError: ${error.code}, ${error.message}, ${error.details}")
                Toast.makeText(applicationContext, "Error fetching data: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun updateUIWithBookingDetails(booking: BookingDetails) {
        // Log the details being set to the UI
        Log.d("BookingSummary", "Updating UI with booking details: $booking")

        // Now update the UI with the booking details
        findViewById<TextView>(R.id.booking_id_textview).text = booking.bookingId.also {
            Log.d("BookingSummary", "Setting Booking ID: $it")
        }
        findViewById<TextView>(R.id.date_textview).text = booking.date.also {
            Log.d("BookingSummary", "Setting Date: $it")
        }
        findViewById<TextView>(R.id.facility_textview).text = booking.facilityName.also {
            Log.d("BookingSummary", "Setting Facility Name: $it")
        }
        findViewById<TextView>(R.id.name_textview).text = booking.name.also {
            Log.d("BookingSummary", "Setting Name: $it")
        }
        findViewById<TextView>(R.id.pax_textview).text = booking.pax.also {
            Log.d("BookingSummary", "Setting Pax: $it")
        }
        findViewById<TextView>(R.id.phone_textview).text = booking.phoneNumber.also {
            Log.d("BookingSummary", "Setting Phone Number: $it")
        }
        findViewById<TextView>(R.id.time_textview).text = booking.timeSlot.also {
            Log.d("BookingSummary", "Setting Time Slot: $it")
        }
        findViewById<TextView>(R.id.user_id_textview).text = booking.userId.also {
            Log.d("BookingSummary", "Setting User ID: $it")
        }
    }

    // Function to cancel booking on the spot
    private fun cancelBooking(bookingId: String) {
        // Get a reference to the bookings node in Firebase
        dbRef = FirebaseDatabase.getInstance().getReference("Bookings")

        // Use the booking ID to reference the specific booking you want to cancel
        dbRef.child(bookingId).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Inform the user that the booking was canceled successfully
                Toast.makeText(applicationContext, "Booking cancelled successfully", Toast.LENGTH_LONG).show()

                // Log the cancellation
                Log.d("BookingSummary", "Booking with ID $bookingId cancelled successfully")

                // Redirect to the homepage
                val intent = Intent(this@BookingSummary, Homepage::class.java)
                startActivity(intent)
                finish() // Close the current activity
            } else {
                // Inform the user that there was an error cancelling the booking
                Toast.makeText(applicationContext, "Failed to cancel booking", Toast.LENGTH_LONG).show()

                // Log the error
                task.exception?.let {
                    Log.e("BookingSummary", "Failed to cancel booking", it)
                }
            }
        }
    }
}
