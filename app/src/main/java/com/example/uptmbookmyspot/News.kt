package com.example.uptmbookmyspot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uptmbookmyspot.Adapter.NewsItemAdapter
import com.example.uptmbookmyspot.Model.NewsItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class News : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsItemAdapter
    private var newsList: MutableList<NewsItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        // Initialize Firebase Database reference
        dbRef = FirebaseDatabase.getInstance().getReference("News")

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewNews)
        recyclerView.layoutManager = LinearLayoutManager(this)
        newsAdapter = NewsItemAdapter(newsList)
        recyclerView.adapter = newsAdapter

        // Retrieve news items from Firebase
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    newsList.clear()
                    for (newsSnapshot in snapshot.children) {
                        val title = newsSnapshot.child("title").getValue(String::class.java)
                        val description = newsSnapshot.child("description").getValue(String::class.java)
                        val pdfUrl = newsSnapshot.child("pdfUrl").getValue(String::class.java)
                        if (title != null && description != null && pdfUrl != null) {
                            val newsItem = NewsItem(title, description, pdfUrl)
                            newsList.add(newsItem)
                        }
                    }
                    newsAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // show error message
            }
        })

        newsAdapter = NewsItemAdapter(newsList)
        recyclerView.adapter = newsAdapter
    }
}