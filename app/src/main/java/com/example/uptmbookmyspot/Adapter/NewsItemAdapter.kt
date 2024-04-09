package com.example.uptmbookmyspot.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uptmbookmyspot.Model.NewsItem
import com.example.uptmbookmyspot.PdfViewer
import com.example.uptmbookmyspot.R

class NewsItemAdapter(private val newsList: List<NewsItem>) : RecyclerView.Adapter<NewsItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newsItem = newsList[position]

        // Set data to views
        holder.textNewsTitle.text = newsItem.title
        holder.textNewsDescription.text = newsItem.description

        // Set OnClickListener to open PDF
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, PdfViewer::class.java)
            intent.putExtra("pdfUrl", newsItem.pdfUrl) // assuming pdfUrl is the URL of the PDF in Firebase Storage
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textNewsTitle: TextView = itemView.findViewById(R.id.textNewsTitle)
        val textNewsDescription: TextView = itemView.findViewById(R.id.textNewsDescription)
    }
}