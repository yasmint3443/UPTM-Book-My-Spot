package com.example.uptmbookmyspot.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uptmbookmyspot.Model.BookingDetails
import com.example.uptmbookmyspot.R

class CalendarAdapter(private val bookingsList: List<BookingDetails>) :
    RecyclerView.Adapter<CalendarAdapter.BookingViewHolder>() {

    class BookingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val facilityNameTextView: TextView = view.findViewById(R.id.tVFacilityName)
        val timeSlotTextView: TextView = view.findViewById(R.id.tVTimeSlot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookingsList[position]
        holder.facilityNameTextView.text = booking.facilityName
        holder.timeSlotTextView.text = booking.timeSlot
    }

    override fun getItemCount() = bookingsList.size
}