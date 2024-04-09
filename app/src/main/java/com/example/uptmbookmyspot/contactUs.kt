package com.example.uptmbookmyspot

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class contactUs : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)
    }

    fun onInstagramClicked(view: View) {
        val instagramUrl = "https://www.instagram.com/uptm_official/"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl))
        startActivity(intent)
    }

    fun onFacebookClicked(view: View) {
        val instagramUrl = "https://www.facebook.com/uptm.official/"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl))
        startActivity(intent)
    }

    fun onTiktokClicked(view: View) {
        val instagramUrl = "https://www.tiktok.com/@uptm_official"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl))
        startActivity(intent)
    }

}