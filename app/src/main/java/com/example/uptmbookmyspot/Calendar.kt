package com.example.uptmbookmyspot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uptmbookmyspot.Adapter.CalendarAdapter
import com.example.uptmbookmyspot.Model.BookingDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Locale

class Calendar : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var bookingsRecyclerView: RecyclerView
    private lateinit var calendarAdapter: CalendarAdapter
    private var bookingsList: MutableList<BookingDetails> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        calendarView = findViewById(R.id.calView)
        bookingsRecyclerView = findViewById(R.id.bookingsRecyclerView)

        bookingsRecyclerView.layoutManager = LinearLayoutManager(this)
        calendarAdapter = CalendarAdapter(bookingsList)
        bookingsRecyclerView.adapter = calendarAdapter

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val cal = java.util.Calendar.getInstance()
            cal.set(year, month, dayOfMonth)
            val dateFormat = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
            val selectedDate = dateFormat.format(cal.time)
            Log.d("CalendarActivity", "Querying date: $selectedDate")
            fetchBookingsForDate(selectedDate)
        }
    }

    private fun fetchBookingsForDate(date: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Bookings")
        dbRef.orderByChild("date").equalTo(date).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("CalendarActivity", "Snapshot exists: ${snapshot.exists()}, Count: ${snapshot.childrenCount}")
                bookingsList.clear() // Clear the list to ensure it only contains bookings for the selected date

                for (postSnapshot in snapshot.children) {
                    val booking = postSnapshot.getValue(BookingDetails::class.java)
                    booking?.let {
                        bookingsList.add(it)
                        Log.d("CalendarActivity", "Added booking: ${it.facilityName}")
                    }
                }

                // Ensure UI updates are performed on the main thread
                runOnUiThread {
                    calendarAdapter.notifyDataSetChanged()
                }

                if (bookingsList.isEmpty()) {
                    Toast.makeText(applicationContext, "No bookings on this day.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                runOnUiThread {
                    Toast.makeText(applicationContext, "Failed to load bookings: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}