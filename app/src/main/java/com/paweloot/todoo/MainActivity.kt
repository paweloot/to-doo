package com.paweloot.todoo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "MainActivity"
private const val TO_DOO_NOTE_KEY = "toDooNote"

class MainActivity : AppCompatActivity() {

    private lateinit var toDooViewModel: ToDooViewModel

    private val collecionRef =
            FirebaseFirestore.getInstance().collection("notes")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toDooViewModel =
                ViewModelProviders.of(this).get(ToDooViewModel::class.java)

        val toDooFragment = ToDooFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .add(R.id.to_doo_container, toDooFragment)
                .commit()

        toDooViewModel.toDooNote.observe(
                this,
                Observer { toDoNote ->
                    if (toDoNote.content.isBlank()) return@Observer

                    collecionRef.add(toDoNote).addOnSuccessListener {
                        Log.d(TAG, "Document has been saved!")
                    }.addOnFailureListener {
                        Log.d(TAG, "Document has NOT been saved!")
                    }
                }
        )
    }
}
