package com.paweloot.todoo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders


class ToDooFragment : Fragment() {

    private lateinit var toDooViewModel: ToDooViewModel

    private lateinit var toDooEditText: EditText
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toDooViewModel =
            ViewModelProviders.of(requireActivity()).get(ToDooViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.fragment_to_doo, container, false)

        toDooEditText = view.findViewById(R.id.to_doo_edit_text)
        addButton = view.findViewById(R.id.add_button)

        addButton.setOnClickListener {
            toDooViewModel.toDooNote.postValue(
                ToDooNote(toDooEditText.text.toString())
            )
        }

        return view
    }

    companion object {
        fun newInstance() = ToDooFragment()
    }
}
