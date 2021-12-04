package com.anatame.pickaflix.presentation.Fragments.Saved

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SavedMoviesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Saved Movie Fragment"
    }
    val text: LiveData<String> = _text
}