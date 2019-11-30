package com.paweloot.todoo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView

class ToDooAdapter(val notes: List<ToDooNote>) :
    RecyclerView.Adapter<ToDooAdapter.ToDooHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDooHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_note, parent, false)

        return ToDooHolder(view)
    }

    override fun onBindViewHolder(holder: ToDooHolder, position: Int) {
        // if it's the last element used to add notes, hide it
        if (position == notes.size - 1) {
            holder.itemView.visibility = View.GONE
        }

        holder.setContent(notes[position])
    }

    override fun getItemCount(): Int = notes.size

    inner class ToDooHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contentEditText =
            itemView.findViewById(R.id.note_content) as EditText

        fun setContent(toDoo: ToDooNote) {
            contentEditText.setText(toDoo.content)
        }
    }
}