package com.anatame.pickaflix.presentation.Fragments.Search

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.anatame.pickaflix.common.Resource
import com.anatame.pickaflix.databinding.FragmentSearchBinding
import com.anatame.pickaflix.presentation.Adapters.SearchRVAdapter
import kotlinx.coroutines.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.anatame.pickaflix.presentation.Fragments.home.HomeFragmentDirections
import androidx.core.content.ContextCompat.getSystemService





class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private var binding: FragmentSearchBinding? = null
    lateinit var searchAdapter: SearchRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        setupRecyclerView()
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding?.svSearchView?.requestFocus()
        binding?.svSearchView?.setOnQueryTextFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                showInputMethod(view.findFocus());
            }
        }

        var job: Job? = null
        binding?.svSearchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                job?.cancel()
                job = viewModel.viewModelScope.launch {
                    delay(500)
                    newText.let {
                        if(it.isNotEmpty()){
                            viewModel.getSearch(it)
                            Log.d("searchTerm", it)
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


        searchAdapter.setOnItemClickListener {searchMovieItem, imageView ->

            val destination = SearchFragmentDirections.actionNavigationSearchToNavigationDetail(
                null,
                imageView.transitionName,
                searchMovieItem,
                null
            )
            val extras = FragmentNavigatorExtras(imageView to imageView.transitionName)

            findNavController().navigate(
                destination,
                extras
            )

            binding?.svSearchView?.setQuery("", false)
        }

        viewModel.searchList.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { movie ->
                        searchAdapter.differ.submitList(movie)
                    }
                }
//                is Resource.Error -> {
//                    hideProgressBar()
//                    response.message?.let { message ->
//                        Log.e(TAG, "An error occured: $message")
//                    }
//                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })




        super.onViewCreated(view, savedInstanceState)
    }

    private fun showInputMethod(view: View) {
        binding?.svSearchView?.isIconified = true;
        binding?.svSearchView?.isIconified = false;
    }

    private fun hideProgressBar() {
//        paginationProgressBar.visibility = View.INVISIBLE
        Toast.makeText(context, "Finished", Toast.LENGTH_SHORT).show()
    }

    private fun showProgressBar() {
//        paginationProgressBar.visibility = View.VISIBLE
        Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
    }


    private fun setupRecyclerView() {
        searchAdapter = SearchRVAdapter(requireContext())
        binding?.searchRV?.apply {
            adapter = searchAdapter
            layoutManager = GridLayoutManager(context, 2)

            postponeEnterTransition()
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }
    }


}