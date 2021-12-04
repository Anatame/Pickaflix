package com.anatame.pickaflix.presentation.Fragments.Search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import com.anatame.pickaflix.R
import com.anatame.pickaflix.databinding.FragmentHomeBinding
import com.anatame.pickaflix.databinding.SearchFragmentBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: SearchFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)


        var job: Job? = null
        binding.svSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                job?.cancel()
                job = MainScope().launch {
                    delay(400)
                    newText.let {
                        if(it.isNotEmpty()){
                            Log.d("search", it)
                        }
                    }
                }

                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                // task HERE
                return false
            }

        })

//            job?.cancel()
//            job = MainScope().launch {
//                delay(SEARCH_NEWS_TIME_DELAY)
//                editable?.let {
//                    if(editable.toString().isNotEmpty()) {
//                        viewModel.searchNews(editable.toString())
//                    }
//                }
//            }
//        }



        super.onViewCreated(view, savedInstanceState)
    }


}