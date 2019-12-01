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

class ToDooAdapter(context: Context, private val toDoos: List<ToDoo>) :
    RecyclerView.Adapter<ToDooAdapter.ToDooHolder>() {

    private val toDooViewModel = ViewModelProviders.of(context as FragmentActivity)
        .get(ToDooViewModel::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDooHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_note, parent, false)

        return ToDooHolder(view)
    }

    override fun onBindViewHolder(holder: ToDooHolder, position: Int) {
        if (position == toDoos.size - 1) {
            setSubmitOnEnter(holder.contentEditText)
            holder.check.visibility = View.GONE
        } else {
            disableEditText(holder.contentEditText)
            holder.bindToDoo(toDoos[position])

            holder.check.setOnCheckedChangeListener { _, isChecked ->
                toDooViewModel.toDoos.value
                    ?.get(position)
                    ?.checked = isChecked
            }
        }
    }

    override fun getItemCount(): Int = toDoos.size

    private fun setSubmitOnEnter(newNote: EditText) {
        newNote.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                toDooViewModel.newToDoo.postValue(
                    ToDoo().apply {
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
        val contentEditText: EditText = itemView.findViewById(R.id.note_content)
        val check: CheckBox = itemView.findViewById(R.id.note_check)

        fun bindToDoo(toDoo: ToDoo) {
            contentEditText.setText(toDoo.content)

            if (toDoo.checked!!) {
                check.isChecked = true
                contentEditText.paintFlags = contentEditText.paintFlags or
                        Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                check.isChecked = false
                contentEditText.paintFlags = contentEditText.paintFlags and
                        Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }
    }
}