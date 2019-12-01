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
private const val NOTES_COLLECTION_KEY = "notes"

class MainActivity : AppCompatActivity() {

    private lateinit var noteViewModel: NoteViewModel

    private val collectionRef =
        FirebaseFirestore.getInstance().collection(NOTES_COLLECTION_KEY)

    private val onNewToDooObserver = Observer<ToDoo> { newToDoo ->
        if (newToDoo.content.isBlank()) return@Observer

        newToDoo.timestamp = Timestamp(Date())

        collectionRef.add(newToDoo).addOnSuccessListener {
            Log.d(TAG, "Document has been saved!")
        }.addOnFailureListener {
            Log.d(TAG, "Document has NOT been saved!")
        }
    }

    override fun onStart() {
        super.onStart()

        collectionRef.addSnapshotListener(this) { querySnapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listening failed", e)
                return@addSnapshotListener
            }

            val fetchedToDoos = mutableListOf<ToDoo>()
            val sortedQuerySnapshot = querySnapshot?.sortedBy { q ->
                q.getTimestamp("timestamp")
            }
            for (doc in sortedQuerySnapshot!!) {
                fetchedToDoos.add(doc.toObject(ToDoo::class.java))
            }

            // Add an empty note to the end of the list
            fetchedToDoos.add(ToDoo().apply { timestamp = Timestamp(Date()) })

            noteViewModel.note.postValue(Note().apply { toDoos = fetchedToDoos })
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
