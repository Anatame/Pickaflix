package com.anatame.pickaflix.presentation.Fragments.Saved

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SavedMoviesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Saved Movies holder to be implemented"
    }
    val text: LiveData<String> = _text
}