package com.paweloot.todoo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NoteViewModel : ViewModel() {

    val newToDoo: MutableLiveData<ToDoo> = MutableLiveData()
//    val notes: MutableLiveData<List<ToDooNote>> = MutableLiveData()

    val note: MutableLiveData<Note> = MutableLiveData()
}