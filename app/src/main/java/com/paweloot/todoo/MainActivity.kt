package com.paweloot.todoo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var noteViewModel: NoteViewModel

    private val db = FirebaseFirestore.getInstance()
    private val noteReference =
        db.document("notes/note")
    private val toDoosReference =
        db.collection("notes/note/toDoos")

    private val onNewToDooObserver = Observer<ToDoo> { newToDoo ->
        if (newToDoo.content.isBlank()) return@Observer

        newToDoo.timestamp = Timestamp(Date())
        toDoosReference.add(newToDoo)
    }

    override fun onStart() {
        super.onStart()

        toDoosReference.addSnapshotListener(this) { querySnapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listening failed", e)
                return@addSnapshotListener
            }

            val sortedQuerySnapshot = querySnapshot?.sortedBy { q ->
                q.getTimestamp("timestamp")
            }
            val fetchedToDoos = mutableListOf<ToDoo>()
            for (doc in sortedQuerySnapshot!!) {
                fetchedToDoos.add(doc.toObject(ToDoo::class.java))
            }

            // Add an empty note to the end of the list
            fetchedToDoos.add(ToDoo().apply { timestamp = Timestamp(Date()) })

            noteViewModel.toDoos.postValue(fetchedToDoos)
        }

        noteReference.addSnapshotListener { documentSnapshot, e ->
            val newTitle = documentSnapshot?.getString("title")
            newTitle.let {
                noteViewModel.noteTitle.postValue(newTitle)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        noteViewModel =
            ViewModelProviders.of(this).get(NoteViewModel::class.java)

        val toDooFragment = ToDooFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .add(R.id.to_doo_container, toDooFragment)
            .commit()

        noteViewModel.newToDoo.observe(this, onNewToDooObserver)
    }
}
