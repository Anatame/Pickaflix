package com.anatame.pickaflix.presentation.Fragments.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.anatame.pickaflix.common.Resource
import com.anatame.pickaflix.databinding.FragmentHomeBinding
import com.anatame.pickaflix.presentation.Adapters.MovieAdapter
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    lateinit var movieAdapter: MovieAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()


        movieAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("movie", it)
            }
        }

        homeViewModel.Movies.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { movie ->
                        movieAdapter.differ.submitList(movie)
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



        return root
    }

    private fun hideProgressBar() {
//        paginationProgressBar.visibility = View.INVISIBLE
        Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
    }

    private fun showProgressBar() {
//        paginationProgressBar.visibility = View.VISIBLE
        Toast.makeText(context, "Finished", Toast.LENGTH_SHORT).show()
    }


    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter()
        binding.rvMovies.apply {
            adapter = movieAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}