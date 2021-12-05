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
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.anatame.pickaflix.R
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


        movieAdapter.setOnItemClickListener { movieItem, imageView ->
//            val bundle = Bundle().apply {
//                putSerializable("movie", movieItem)
//                putString("imageID", imageView.transitionName)
//            }
            val destination = HomeFragmentDirections.actionNavigationHomeToMovieDetailFragment(
                movieItem,
                imageView.transitionName
            )
            val extras = FragmentNavigatorExtras(imageView to imageView.transitionName)

            findNavController().navigate(
                destination,
                extras
            )

        }

        homeViewModel.Movies.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { movie ->
                        movieAdapter.differ.submitList(movie)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        binding.ibSearchButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_searchFragment)
        }



        return root
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
        movieAdapter = MovieAdapter()
        binding.rvMovies.apply {
            adapter = movieAdapter
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            postponeEnterTransition()
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}