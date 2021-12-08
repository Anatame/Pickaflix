package com.anatame.pickaflix.domain.models

import androidx.lifecycle.MutableLiveData

class NestedScrollState (
    val scrollState1: MutableLiveData<Int>,
    val scrollState2: MutableLiveData<Int>,
    val scrollState3: MutableLiveData<Int>,
    val scrollState4: MutableLiveData<Int>,
    val scrollState5: MutableLiveData<Int>,
)