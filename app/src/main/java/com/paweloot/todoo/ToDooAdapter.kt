package com.paweloot.todoo

import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView

class ToDooAdapter(val context: Context, val notes: List<ToDooNote>) :
    RecyclerView.Adapter<ToDooAdapter.ToDooHolder>() {

    private val toDooViewModel = ViewModelProviders.of(context as FragmentActivity)
        .get(ToDooViewModel::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDooHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_note, parent, false)

        return ToDooHolder(view)
    }

    override fun onBindViewHolder(holder: ToDooHolder, position: Int) {
        // if it's the last element used to add notes, hide it
        if (position == notes.size - 1) {
            setSubmitOnEnter(holder)
            holder.contentEditText.text.clear()
        }

        holder.setContent(notes[position])
    }

    override fun getItemCount(): Int = notes.size

    private fun setSubmitOnEnter(holder: ToDooHolder) {
        val newNote: EditText = holder.contentEditText

        newNote.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER
                && event.action == KeyEvent.ACTION_UP
                && newNote.text.isNotBlank()
            ) {
                toDooViewModel.newNote.postValue(
                    ToDooNote().apply {
                        content = newNote.text.toString()
                    }
                )

                newNote.clearFocus()

                return@setOnKeyListener true
            }

            false
        }
    }

    inner class ToDooHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contentEditText =
            itemView.findViewById(R.id.note_content) as EditText

        fun setContent(toDoo: ToDooNote) {
            contentEditText.setText(toDoo.content)
        }
    }
}