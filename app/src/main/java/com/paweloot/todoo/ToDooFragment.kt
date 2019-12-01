package com.paweloot.todoo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ToDooFragment : Fragment() {

    private lateinit var toDooViewModel: ToDooViewModel

    private lateinit var noteTitle: TextView
    private lateinit var toDooList: RecyclerView

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

        noteTitle = view.findViewById(R.id.note_title)
        toDooList = view.findViewById(R.id.to_doo_list)

        noteTitle.text = "TODO"

        toDooList.layoutManager = LinearLayoutManager(context)
        toDooViewModel.notes.observe(
            viewLifecycleOwner,
            Observer<List<ToDooNote>> { notes ->
                toDooList.adapter = ToDooAdapter(requireContext(), notes)
            }
        )

        return view
    }

    companion object {
        fun newInstance() = ToDooFragment()
    }
}
