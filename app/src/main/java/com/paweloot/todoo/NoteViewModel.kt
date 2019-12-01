package com.paweloot.todoo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NoteViewModel : ViewModel() {

    val newToDoo: MutableLiveData<ToDoo> = MutableLiveData()

    val noteTitle: MutableLiveData<String> = MutableLiveData()
    val toDoos: MutableLiveData<List<ToDoo>> = MutableLiveData()
}