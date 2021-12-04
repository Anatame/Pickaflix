package com.anatame.pickaflix.presentation.Fragments.Search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anatame.pickaflix.data.remote.PageParser.Home.Parser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel (
    private val parser: Parser = Parser()
) : ViewModel() {

    fun getSearch(searchTerm: String){
        viewModelScope.launch(Dispatchers.IO){
            parser.getSearchItem(searchTerm)
        }
    }
}