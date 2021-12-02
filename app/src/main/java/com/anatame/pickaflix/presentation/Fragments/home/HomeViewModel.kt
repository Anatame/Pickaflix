package com.anatame.pickaflix.presentation.Fragments.home


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anatame.pickaflix.data.remote.PageParser.Home.Parser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val parser: Parser
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    val text: LiveData<String> = _text

    fun getHomeScreenData(){
        viewModelScope.launch (Dispatchers.IO) {
            parser.getMovieList()

            withContext(Dispatchers.Main){
                // send list to recycler view
            }
        }
    }
}