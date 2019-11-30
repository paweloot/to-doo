package com.paweloot.todoo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.firestore.FirebaseFirestore

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

        val db = FirebaseFirestore.getInstance()

        toDooViewModel.toDooNote.observe(
                this,
                Observer { toDoNote ->

                }
        )
    }
}
