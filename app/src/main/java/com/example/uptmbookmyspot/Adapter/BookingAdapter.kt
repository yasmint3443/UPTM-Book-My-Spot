package com.example.uptmbookmyspot.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uptmbookmyspot.Model.BookingDetails
import com.example.uptmbookmyspot.R

class BookingAdapter (private val bookingsList: List<BookingDetails>) :
    RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    class BookingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvFacilityName: TextView = view.findViewById(R.id.tvFacilityName)
        val tvBookingDate: TextView = view.findViewById(R.id.tvBookingDate)
        val tvBookingTime: TextView = view.findViewById(R.id.tvBookingTime)
        val tvBookingPax: TextView = view.findViewById(R.id.tvBookingPax)
        val tvBookingId: TextView = view.findViewById(R.id.tvBookingId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_booking, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookingsList[position]
        Log.d("BookingAdapter", "Binding booking: $booking")
        holder.tvFacilityName.text = booking.facilityName
        holder.tvBookingDate.text = booking.date
        holder.tvBookingTime.text = booking.timeSlot
        holder.tvBookingPax.text = booking.pax
        holder.tvBookingId.text = booking.bookingId
    }

    override fun getItemCount() = bookingsList.size
}