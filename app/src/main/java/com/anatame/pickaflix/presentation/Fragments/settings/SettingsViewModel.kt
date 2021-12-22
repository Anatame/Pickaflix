package com.anatame.pickaflix.presentation.Fragments.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Settings holder to be implemented"
    }
    val text: LiveData<String> = _text
}