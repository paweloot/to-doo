package com.paweloot.todoo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ToDooViewModel : ViewModel() {

    val toDooNote: MutableLiveData<ToDooNote> = MutableLiveData()
    val notes: MutableLiveData<List<ToDooNote>> = MutableLiveData()
}