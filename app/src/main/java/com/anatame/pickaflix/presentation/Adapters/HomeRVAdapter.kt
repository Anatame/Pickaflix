package com.anatame.pickaflix.presentation.Adapters

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat.postponeEnterTransition
import androidx.core.app.ActivityCompat.startPostponedEnterTransition
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anatame.pickaflix.common.Resource
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.MovieItem
import com.anatame.pickaflix.domain.models.HomeItem
import com.anatame.pickaflix.databinding.ItemHeroViewpagerHolderBinding
import com.anatame.pickaflix.databinding.ItemHomeCategoryBinding
import com.anatame.pickaflix.presentation.Fragments.home.HomeFragmentDirections

class HomeRVAdapter(
    val activity: Context,
    val lifeCycleOwner: LifecycleOwner,
    val homeItemList: List<HomeItem>,
    val scrollState: MutableLiveData<Int>
):  RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewPagerViewHolder(
        val pagerBinding: ItemHeroViewpagerHolderBinding
    ):  RecyclerView.ViewHolder(pagerBinding.root)

    inner class CategoryViewHolder(
        val rvItemBinding: ItemHomeCategoryBinding
    ): RecyclerView.ViewHolder(rvItemBinding.root)

    override fun getItemViewType(position: Int): Int {
        return when(position){
            0 -> 0
            1 -> 1
            2 -> 2
            3 -> 3
            4 -> 4
            else -> 5
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewPagerBinding =  ItemHeroViewpagerHolderBinding
            .inflate(LayoutInflater
                .from(parent.context), parent, false)

        val categoryItemBinding = ItemHomeCategoryBinding
            .inflate(LayoutInflater
                .from(parent.context), parent, false)

        return when(viewType){
            0 -> {
                ViewPagerViewHolder(viewPagerBinding)
            }

            else -> {
                CategoryViewHolder(categoryItemBinding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
     //   Toast.makeText(activity, "home size $itemCount", Toast.LENGTH_SHORT).show()
        when(holder.itemViewType){
            0 -> {
                holder as ViewPagerViewHolder
                val adapter = HeroPagerAdapter(activity)
                holder.pagerBinding.vpHeroPager.adapter = adapter
                homeItemList[position].pagerList!!.observe(lifeCycleOwner, Observer { response ->
                    when(response) {
                        is Resource.Success -> {
                            response.data?.let { movie ->
                                Toast.makeText(activity, "Finished", Toast.LENGTH_SHORT)
                                    .show()
                                adapter.differ.submitList(movie)
                            }
                        }
                        is Resource.Loading -> {
                            Toast.makeText(activity, "Loading", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                })

            }
            1 -> {
                categoryItemHolderSetup(
                    holder,
                    position,
                    "Trending Movies",
                    homeItemList[position].categoryItem!!.movieItemList
                )
            }
            2 -> {
                categoryItemHolderSetup(
                    holder,
                    position,
                    "Popular Shows",
                    homeItemList[position].categoryItem!!.trendingShows
                )
            }
            3 -> {
                categoryItemHolderSetup(
                    holder,
                    position,
                    "Latest Movies",
                    homeItemList[position].categoryItem!!.latestMovies
                )
            }
            4 -> {
                categoryItemHolderSetup(
                    holder,
                    position,
                    "New TV Shows",
                    homeItemList[position].categoryItem!!.latestShows
                )
            }
            5 -> {
                categoryItemHolderSetup(
                    holder,
                    position,
                    "Coming Soon!",
                    homeItemList[position].categoryItem!!.comingSoon
                )
            }
        }
    }

    private fun categoryItemHolderSetup(
        holder: RecyclerView.ViewHolder,
        position: Int,
        itemName: String,
        itemList: MutableLiveData<Resource<List<MovieItem>>>,
    ) {
        holder as CategoryViewHolder
        holder.rvItemBinding.apply {
            tvCategoryName.text = itemName

            val adapter = MovieAdapter(activity)
            rvCategoryItems.adapter = adapter
            rvCategoryItems.layoutManager = LinearLayoutManager(
                activity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            rvCategoryItems.setHasFixedSize(true);
            rvCategoryItems.setNestedScrollingEnabled(false);
            scrollState.observe(lifeCycleOwner, Observer{
                (rvCategoryItems.layoutManager as LinearLayoutManager)
                    .scrollToPositionWithOffset(it, 200)
            })


//            rvCategoryItems.apply {
//                postponeEnterTransition(activity as Activity)
//                viewTreeObserver.addOnPreDrawListener {
//                    startPostponedEnterTransition(activity as Activity)
//                    true
//                }
//            }

//            homeItemList[position].categoryItem!!.movieItemList
            itemList.observe(
                lifeCycleOwner,
                Observer { response ->
                    when (response) {
                        is Resource.Success -> {
                            response.data?.let { movie ->
                                adapter.differ.submitList(movie)
                            }
                        }
                        is Resource.Loading -> {
                        }
                    }
                })

            adapter.setOnItemClickListener {position, movieItem, imageView ->
                startNavigation(holder.itemView, movieItem, imageView)
                Handler(Looper.getMainLooper()).postDelayed({
                    //doSomethingHere()
                }, 300)
                scrollState.postValue(position)
            }
        }
    }

    fun startNavigation(view: View, movieItem: MovieItem, imageView: ImageView){

        val destination = HomeFragmentDirections.actionNavigationHomeToMovieDetailFragment(
            movieItem,
            imageView.transitionName,
            null
        )
        val extras = FragmentNavigatorExtras(imageView to imageView.transitionName)

        view.findNavController().navigate(
            destination,
            extras
        )
    }


    override fun getItemCount(): Int {
        return homeItemList.size
    }

}