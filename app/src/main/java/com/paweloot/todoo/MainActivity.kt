package com.paweloot.todoo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var toDooViewModel: ToDooViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toDooViewModel =
            ViewModelProviders.of(this).get(ToDooViewModel::class.java)

        val toDooFragment = ToDooFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .add(R.id.to_doo_container, toDooFragment)
            .commit()
    }

    override fun onStart() {
        super.onStart()

        FirebaseFirestore.getInstance()
            .collection("notes/note/toDoos")
            .get()
            .addOnSuccessListener { documents ->
                val fetchedToDoos: MutableList<ToDoo> = mutableListOf()
                val sortedDocuments = documents?.sortedBy { d ->
                    d.getTimestamp("timestamp")
                }

                for (document in sortedDocuments!!) {
                    val toDoo = document.toObject(ToDoo::class.java).apply {
                        id = document.id
                    }

                    fetchedToDoos.add(toDoo)
                }

                fetchedToDoos.add(ToDoo().apply { timestamp = Timestamp(Date()) })
                toDooViewModel.toDoos.postValue(fetchedToDoos)
            }
    }
}
