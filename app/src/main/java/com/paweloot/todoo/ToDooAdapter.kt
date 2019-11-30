package com.paweloot.todoo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ToDooAdapter(private val notes: List<ToDooNote>) :
    RecyclerView.Adapter<ToDooAdapter.ToDooHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDooHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)

        return ToDooHolder(view)
    }

    override fun onBindViewHolder(holder: ToDooHolder, position: Int) {
        holder.setContent(notes[position])
    }

    override fun getItemCount(): Int = notes.size

    inner class ToDooHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contentTextView = itemView as TextView

        fun setContent(toDoo: ToDooNote) {
            contentTextView.text = toDoo.content
        }
    }
}