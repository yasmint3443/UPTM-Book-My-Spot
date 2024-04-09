package com.example.uptmbookmyspot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.example.uptmbookmyspot.Adapter.FaqAdapter
import com.example.uptmbookmyspot.Model.FaqItem

class Faq : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq)

        val faqList = listOf(
            FaqItem("What is the UPTM Book My Spot app?",
                "The UPTM Book My Spot app is a mobile application for booking campus facilities at Universiti Poly-Tech Malaysia."),
            FaqItem("What facilities can I book using the UPTM Book My Spot app?",
                "With the UPTM Book My Spot app, users can easily book a range of facilities available on campus, including lecture halls for academic events, meeting rooms for group discussions or presentations, and sports centers for recreational activities."),
            FaqItem("Is the UPTM Book My Spot app available for both students and staff?",
                "The UPTM Book My Spot app caters to both students and staff members of Universiti Poly-Tech Malaysia, providing an inclusive platform for all members of the university community to make facility reservations conveniently."),
            FaqItem("Can I see real-time availability of facilities through the app?",
                "Through real-time updates, the UPTM Book My Spot app offers users immediate visibility into the availability status of campus facilities, enabling them to make informed booking decisions efficiently."),
            FaqItem("Can I cancel my reservation through the app?",
                "Users have the flexibility to cancel their reservations directly through the UPTM Book My Spot app, providing convenience and control over their bookings."),
            FaqItem("Is there a fee for using the UPTM Book My Spot app?",
                "The UPTM Book My Spot app is offered to users free of charge, ensuring accessibility to all members of the university community without any associated fees or costs."),
            FaqItem("How do I make a reservation using the UPTM Book My Spot app?",
                "To make a reservation using the UPTM Book My Spot app, users simply need to select their desired facility, specify the preferred date and time, and confirm the booking request within the app's interface."),
        )

        val listView = findViewById<ListView>(R.id.listView)
        val adapter = FaqAdapter(this, faqList)
        listView.adapter = adapter
    }
}