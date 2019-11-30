package com.paweloot.todoo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toDooFragment = ToDooFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .add(R.id.to_doo_container, toDooFragment)
                .commit()

        val db = FirebaseFirestore.getInstance()
    }
}
