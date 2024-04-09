package com.example.uptmbookmyspot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListAdapter
import android.widget.ListView
import com.example.uptmbookmyspot.Adapter.FacilityAdapter
import com.example.uptmbookmyspot.Model.Facility
import com.example.uptmbookmyspot.Model.ListData
import com.example.uptmbookmyspot.databinding.ActivityFacilityListBinding
import com.example.uptmbookmyspot.databinding.ActivityMainBinding

class facilityList : AppCompatActivity() {

    private lateinit var binding: ActivityFacilityListBinding
    private lateinit var facilityAdapter: FacilityAdapter
    private lateinit var listData: ListData
    private var dataArrayList = ArrayList<ListData?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facility_list)

        // Inflate the view binding
        binding = ActivityFacilityListBinding.inflate(layoutInflater)
        // Set the content view to the root of the view binding
        setContentView(binding.root)

        // Initialize data
        val imageList = intArrayOf(
            R.drawable.badminton,
            R.drawable.futsal1,
            R.drawable.dewan,
            R.drawable.lab,
            R.drawable.lab,
            R.drawable.lab,
            R.drawable.micro,
            R.drawable.music,
            R.drawable.nilam,
            R.drawable.photo,
            R.drawable.studsquare,
        )

        val nameList = arrayOf("Badminton Court", "Futsal Court", "Multipurpose Hall", "Lab 1", "Lab 2", "Lab 3", "Micro-Teaching Room", "Music Studio", "Nilam Hall", "Photography Studio", "Student Square")

        // Populate dataArrayList
        for (i in nameList.indices) {
            val facility = ListData(nameList[i], "Book", imageList[i]) // Since all items use "Book"
            dataArrayList.add(facility)
        }

        // Initialize FacilityAdapter with the context and the populated dataArrayList
        facilityAdapter = FacilityAdapter(this, dataArrayList)

        // Set the adapter to the ListView using View Binding
        binding.listview.adapter = facilityAdapter

    }
}