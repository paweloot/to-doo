package com.paweloot.todoo

import android.content.Context
import android.graphics.Paint
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.CheckBox
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

        val contentEditText: EditText = view.findViewById(R.id.note_content)
        val noteCheckBox: CheckBox = view.findViewById(R.id.note_check)
        noteCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                contentEditText.paintFlags = contentEditText.paintFlags or
                        Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                contentEditText.paintFlags = contentEditText.paintFlags and
                        Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

        return ToDooHolder(view)
    }

    override fun onBindViewHolder(holder: ToDooHolder, position: Int) {
        // if it's the last element used to add notes, hide it
        if (position == notes.size - 1) {
            holder.noteCheck.visibility = View.GONE
            setSubmitOnEnter(holder.contentEditText)
        } else {
            disableEditText(holder.contentEditText)
            holder.setContent(notes[position])
        }
    }

    override fun getItemCount(): Int = notes.size

    private fun setSubmitOnEnter(newNote: EditText) {
        newNote.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                toDooViewModel.newNote.postValue(
                    ToDooNote().apply {
                        content = newNote.text.toString()
                    }
                )

                newNote.clearFocus()
                newNote.text.clear()

                return@setOnEditorActionListener true
            }

            false
        }
    }

    private fun disableEditText(field: EditText) {
        field.apply {
            isFocusable = false
            inputType = InputType.TYPE_NULL
            setBackgroundResource(android.R.color.transparent)
        }
    }

    inner class ToDooHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contentEditText =
            itemView.findViewById(R.id.note_content) as EditText
        val noteCheck: CheckBox =
            itemView.findViewById(R.id.note_check)

        fun setContent(toDoo: ToDooNote) {
            contentEditText.setText(toDoo.content)
        }
    }
}