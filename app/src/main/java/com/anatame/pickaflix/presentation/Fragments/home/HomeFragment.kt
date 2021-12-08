package com.anatame.pickaflix.presentation.Fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.anatame.pickaflix.R
import com.anatame.pickaflix.databinding.FragmentHomeBinding
import com.anatame.pickaflix.domain.models.CategoryItem
import com.anatame.pickaflix.domain.models.HomeItem
import com.anatame.pickaflix.presentation.Adapters.HomeRVAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    lateinit var homeRvAdapter: HomeRVAdapter
    lateinit var homeRvItemList: ArrayList<HomeItem>

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        homeViewModel =
            ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeRvItemList = ArrayList()
        setupRecyclerView()

        val categoryName: String = "Trending"

        var movieItemList = homeViewModel.movies
        var pagerList = homeViewModel.sliderItems
        var trendingShows = homeViewModel.trendingShows
        var latestMovies = homeViewModel.latestMovies
        var latestShows = homeViewModel.latestShows
        var comingSoon = homeViewModel.comingSoon


        val categoryItem: CategoryItem = CategoryItem(
            movieItemList,
            trendingShows,
            latestMovies,
            latestShows,
            comingSoon
        )

        homeRvItemList.add(
            HomeItem(
                pagerList,
                null
            )
        )

        for(i in 1..5){
            homeRvItemList.add(
                HomeItem(
                    null,
                    categoryItem
                )
            )
        }


//
//        homeRvAdapter = context?.let { HomeRVAdapter(it, homeRvItemList) }!!
//        setupRecyclerView()

//
//        movieAdapter.setOnItemClickListener { movieItem, imageView ->
////            val bundle = Bundle().apply {
////                putSerializable("movie", movieItem)
////                putString("imageID", imageView.transitionName)
////            }
//            val destination = HomeFragmentDirections.actionNavigationHomeToMovieDetailFragment(
//                movieItem,
//                imageView.transitionName,
//                null
//            )
//            val extras = FragmentNavigatorExtras(imageView to imageView.transitionName)
//
//            findNavController().navigate(
//                destination,
//                extras
//            )
//
//        }
//





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

        homeRvAdapter = HomeRVAdapter(
            requireContext(),
            viewLifecycleOwner,
            homeRvItemList
        )

        binding.rvHome.apply {
            adapter =  homeRvAdapter
            layoutManager = LinearLayoutManager(context)

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