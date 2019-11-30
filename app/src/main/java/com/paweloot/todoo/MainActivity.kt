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

    private lateinit var toDooViewModel: ToDooViewModel

    private val collectionRef =
        FirebaseFirestore.getInstance().collection(NOTES_COLLECTION_KEY)

    override fun onStart() {
        super.onStart()

        collectionRef.addSnapshotListener(this) { querySnapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listening failed", e)
                return@addSnapshotListener
            }

            val notes = mutableListOf<ToDooNote>()
            val sortedQuerySnapshot = querySnapshot?.sortedBy { q ->
                q.getTimestamp("timestamp")
            }
            for (doc in sortedQuerySnapshot!!) {
                notes.add(doc.toObject(ToDooNote::class.java))
            }

            // Add an empty note to the end of the list
            notes.add(ToDooNote().apply { timestamp = Timestamp(Date()) })

            toDooViewModel.notes.postValue(notes)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toDooViewModel =
            ViewModelProviders.of(this).get(ToDooViewModel::class.java)

        val toDooFragment = ToDooFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .add(R.id.to_doo_container, toDooFragment)
            .commit()

        toDooViewModel.newNote.observe(
            this,
            Observer { newNote ->
                if (newNote.content.isBlank()) return@Observer

                newNote.timestamp = Timestamp(Date())

                collectionRef.add(newNote).addOnSuccessListener {
                    Log.d(TAG, "Document has been saved!")
                }.addOnFailureListener {
                    Log.d(TAG, "Document has NOT been saved!")
                }
            }
        )
    }
}
