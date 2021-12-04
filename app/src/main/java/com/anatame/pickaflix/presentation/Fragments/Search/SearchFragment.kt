package com.anatame.pickaflix.presentation.Fragments.Search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import com.anatame.pickaflix.common.Resource
import com.anatame.pickaflix.databinding.FragmentSearchBinding
import com.anatame.pickaflix.presentation.Adapters.SearchRVAdapter
import kotlinx.coroutines.*

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private var binding: FragmentSearchBinding? = null
    lateinit var searchAdapter: SearchRVAdapter
  //  private lateinit var binding: SearchFragmentBinding

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

        searchAdapter.setOnItemClickListener {
//            val bundle = Bundle().apply {
//                putSerializable("movie", it)
//            }
//
//            findNavController().navigate(
//                R.id.action_navigation_home_to_movieDetailFragment,
//                bundle
//            )

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

    private fun hideProgressBar() {
//        paginationProgressBar.visibility = View.INVISIBLE
        Toast.makeText(context, "Finished", Toast.LENGTH_SHORT).show()
    }

    private fun showProgressBar() {
//        paginationProgressBar.visibility = View.VISIBLE
        Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
    }


    private fun setupRecyclerView() {
        searchAdapter = SearchRVAdapter()
        binding?.searchRV?.apply {
            adapter = searchAdapter
            layoutManager = GridLayoutManager(context, 3)
        }
    }


}