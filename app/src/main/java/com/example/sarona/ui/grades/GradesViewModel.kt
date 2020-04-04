package com.example.sarona.ui.grades

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GradesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is grades Fragment"
    }
    val text: LiveData<String> = _text
}