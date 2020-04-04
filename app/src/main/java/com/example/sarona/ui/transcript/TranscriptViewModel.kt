package com.example.sarona.ui.transcript

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TranscriptViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is transcript Fragment"
    }
    val text: LiveData<String> = _text
}