package com.example.uptmbookmyspot

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.uptmbookmyspot.Model.NewsItem
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AdminNews : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var btnInsert: Button
    private lateinit var btnSelectFile: Button
    private var pdfUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_news)

        dbRef = FirebaseDatabase.getInstance().getReference("News")
        btnInsert = findViewById<Button>(R.id.btnInsert)
        btnSelectFile = findViewById<Button>(R.id.btnSelectFile)

        btnSelectFile.setOnClickListener {
            // Intent to select a file
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            startActivityForResult(intent, 1)
        }

        btnInsert.setOnClickListener {
            val title: String = findViewById<EditText>(R.id.editTextTitle).text.toString().trim()
            val description: String = findViewById<EditText>(R.id.editTextDescription).text.toString().trim()
            uploadFile(title, description)
        }
    }

    private fun uploadFile(title: String, description: String) {
        if (pdfUri != null) {
            val storageReference = FirebaseStorage.getInstance().getReference("uploads/${System.currentTimeMillis()}.pdf")
            storageReference.putFile(pdfUri!!).addOnSuccessListener { uploadTask ->
                uploadTask.storage.downloadUrl.addOnSuccessListener { uri ->
                    val pdfUrl = uri.toString()
                    val newsId = dbRef.push().key
                    val newsItem = NewsItem(title, description, pdfUrl)
                    if (newsId != null) {
                        dbRef.child(newsId).setValue(newsItem).addOnCompleteListener { newsTask ->
                            if (newsTask.isSuccessful) {
                                Toast.makeText(applicationContext, "News added successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(applicationContext, "Failed to add news", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.data != null) {
            pdfUri = data.data // The uri with the location of the file
            // Enable the Upload File button
            btnInsert.isEnabled = true
            // Show Toast message indicating file is uploaded
            Toast.makeText(this, "File is uploaded", Toast.LENGTH_SHORT).show()
        }
    }
}