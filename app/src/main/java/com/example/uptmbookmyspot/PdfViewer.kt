package com.example.uptmbookmyspot

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

class PdfViewer : AppCompatActivity() {

    private lateinit var webViewPdf: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)

        val pdfUrl = intent.getStringExtra("pdfUrl")
        if (!pdfUrl.isNullOrEmpty()) {
            openPdfInBrowser(pdfUrl)
        } else {
            // Show error message
            Toast.makeText(this, "Invalid PDF URL", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    private fun openPdfInBrowser(pdfUrl: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl))
        startActivity(browserIntent)
        finish()
    }
}