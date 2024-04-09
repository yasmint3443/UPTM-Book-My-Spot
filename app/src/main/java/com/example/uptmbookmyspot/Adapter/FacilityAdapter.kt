package com.example.uptmbookmyspot.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.example.uptmbookmyspot.BookingForm
import com.example.uptmbookmyspot.Model.Facility
import com.example.uptmbookmyspot.Model.ListData
import com.example.uptmbookmyspot.R
import com.google.android.material.imageview.ShapeableImageView

class FacilityAdapter(private val context: Context, private val facilities: List<ListData?>) : BaseAdapter() {

    override fun getCount(): Int {
        return facilities.size
    }

    override fun getItem(position: Int): Any {
        return facilities[position] ?: throw IndexOutOfBoundsException("Position exceeds dataset size.")
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val facility = facilities[position] ?: return view
        // Set the facility name
        viewHolder.facilityNameTextView.text = facility.name

        // Set the image
        viewHolder.facilityImageView.setImageResource(facility.image)

        // Set the onClickListener for the booking button
        viewHolder.bookButton.setOnClickListener {
            // Handle booking button click
            val intent = Intent(context, BookingForm::class.java)
            intent.putExtra("FACILITY_NAME", facilities[position]?.name)
            context.startActivity(intent)
        }

        return view
    }

    private class ViewHolder(view: View) {
        val facilityImageView: ShapeableImageView = view.findViewById(R.id.listImage)
        val facilityNameTextView: TextView = view.findViewById(R.id.facility_name)
        val bookButton: Button = view.findViewById(R.id.btnBook)
    }
}