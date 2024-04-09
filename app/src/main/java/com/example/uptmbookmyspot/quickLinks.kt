package com.example.uptmbookmyspot

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView

class quickLinks : AppCompatActivity() {

    private lateinit var cms: CardView
    private lateinit var lms: CardView
    private lateinit var calendar: CardView
    private lateinit var timetable: CardView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quick_links)

        cms = findViewById(R.id.cmsCard)
        lms = findViewById(R.id.lmsCard)
        calendar = findViewById(R.id.academicCalendarCard)
        timetable = findViewById(R.id.timetableCard)

        cms.setOnClickListener {
            openWebPage("https://www.kptm.edu.my/ms/cms-pelajar-uptm.html")
        }

        lms.setOnClickListener {
            openWebPage("https://lms.uptm.edu.my/1123/login/index.php")
        }

        calendar.setOnClickListener {
            openWebPage("https://www.uptm.edu.my/index.php/students/academic-calendar")
        }

        timetable.setOnClickListener {
            openWebPage("https://uptm.edupage.org/timetable/")
        }
    }

    private fun openWebPage(url: String) {
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(webIntent)
    }
}