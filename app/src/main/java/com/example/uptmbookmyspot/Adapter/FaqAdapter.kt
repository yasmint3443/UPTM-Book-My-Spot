package com.example.uptmbookmyspot.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.uptmbookmyspot.Model.FaqItem
import com.example.uptmbookmyspot.R

class FaqAdapter (context: Context, faqList: List<FaqItem>) :
    ArrayAdapter<FaqItem>(context, 0, faqList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(
                R.layout.faq_item, parent, false
            )
        }

        val currentItem = getItem(position)

        val questionTextView = listItemView!!.findViewById<TextView>(R.id.questionTextView)
        questionTextView.text = currentItem!!.question

        val answerTextView = listItemView.findViewById<TextView>(R.id.answerTextView)
        answerTextView.text = currentItem.answer

        return listItemView
    }
}