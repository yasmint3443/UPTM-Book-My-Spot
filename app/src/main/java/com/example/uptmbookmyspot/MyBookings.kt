package com.example.uptmbookmyspot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.uptmbookmyspot.Adapter.BookingAdapter
import com.example.uptmbookmyspot.Model.BookingDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth

class MyBookings : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private var bookingsList: MutableList<BookingDetails> = mutableListOf()
    private lateinit var adapter: BookingAdapter
    private lateinit var addBookingButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_bookings)

        recyclerView = findViewById(R.id.bookingsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = BookingAdapter(bookingsList)
        recyclerView.adapter = adapter
        addBookingButton = findViewById(R.id.addBookingButton)

        addBookingButton.setOnClickListener {
            // Navigate to facility list
            val intent = Intent(this, facilityList::class.java)
            startActivity(intent)
        }

        fetchBookings()
    }

    private fun fetchBookings() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUserId = currentUser?.uid

        val dbRef = FirebaseDatabase.getInstance().getReference("Bookings")

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                bookingsList.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val booking = postSnapshot.getValue(BookingDetails::class.java)
                    booking?.let {
                        // Only add bookings made by the current user
                        if (it.userId == currentUserId) {
                            bookingsList.add(it)
                        }
                    }
                }
                Log.d("MyBookings", "Total Bookings fetched: ${bookingsList.size}")
                adapter.notifyDataSetChanged() // Notify the adapter that the data set has changed
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle potential errors
                Log.w("MyBookings", "loadBookings:onCancelled", databaseError.toException())
                Toast.makeText(baseContext, "Failed to load bookings.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}