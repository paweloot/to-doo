package com.paweloot.todoo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ToDooViewModel : ViewModel() {

    val newToDoo: MutableLiveData<ToDoo> = MutableLiveData()
    val toDoos: MutableLiveData<List<ToDoo>> = MutableLiveData()
}