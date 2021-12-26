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
import com.anatame.pickaflix.MainActivity
import com.anatame.pickaflix.common.Resource
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.HeroItem
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.MovieItem
import com.anatame.pickaflix.domain.models.HomeItem
import com.anatame.pickaflix.databinding.ItemHeroViewpagerHolderBinding
import com.anatame.pickaflix.databinding.ItemHomeCategoryBinding
import com.anatame.pickaflix.domain.models.NestedScrollState
import com.anatame.pickaflix.presentation.Fragments.home.HomeFragmentDirections

class HomeRVAdapter(
    val activity: Context,
    val lifeCycleOwner: LifecycleOwner,
    val homeItemList: List<HomeItem>,
    val nestedScrollState: NestedScrollState,
    val viewPagerScrollState: MutableLiveData<Int>
):  RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewPagerViewHolder(
        val pagerBinding: ItemHeroViewpagerHolderBinding
    ):  RecyclerView.ViewHolder(pagerBinding.root)

    inner class CategoryViewHolder(
        val rvItemBinding: ItemHomeCategoryBinding
    ): RecyclerView.ViewHolder(rvItemBinding.root){
        val adapter = MovieAdapter(activity)
    }

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

                viewPagerScrollState.observe(lifeCycleOwner, Observer{
                    holder.pagerBinding.vpHeroPager.setCurrentItem(it, false)
                })

                adapter.setOnItemClickListener {position, heroItem, imageView ->
                    startHeroNavigation(holder.itemView, heroItem, imageView)
                    Handler(Looper.getMainLooper()).postDelayed({
                        viewPagerScrollState.postValue(position)
                    }, 300)
                }

            }
            1 -> {
                categoryItemHolderSetup(
                    holder,
                    position,
                    "Trending Movies",
                    homeItemList[position].categoryItem!!.movieItemList,
                    nestedScrollState.scrollState1
                )
            }
            2 -> {
                categoryItemHolderSetup(
                    holder,
                    position,
                    "Popular Shows",
                    homeItemList[position].categoryItem!!.trendingShows,
                    nestedScrollState.scrollState2
                )
            }
            3 -> {
                categoryItemHolderSetup(
                    holder,
                    position,
                    "Latest Movies",
                    homeItemList[position].categoryItem!!.latestMovies,
                    nestedScrollState.scrollState3
                )
            }
            4 -> {
                categoryItemHolderSetup(
                    holder,
                    position,
                    "New TV Shows",
                    homeItemList[position].categoryItem!!.latestShows,
                    nestedScrollState.scrollState4
                )
            }
            5 -> {
                categoryItemHolderSetup(
                    holder,
                    position,
                    "Coming Soon!",
                    homeItemList[position].categoryItem!!.comingSoon,
                    nestedScrollState.scrollState5
                )
            }
        }
    }

    private fun categoryItemHolderSetup(
        holder: RecyclerView.ViewHolder,
        position: Int,
        itemName: String,
        itemList: MutableLiveData<Resource<List<MovieItem>>>,
        scrollState: MutableLiveData<Int>
    ) {
        holder as CategoryViewHolder

        holder.rvItemBinding.apply {
            tvCategoryName.text = itemName

            rvCategoryItems.adapter = holder.adapter
            rvCategoryItems.layoutManager = LinearLayoutManager(
                activity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            rvCategoryItems.setHasFixedSize(true);
            rvCategoryItems.isNestedScrollingEnabled = false;

            scrollState.observe(lifeCycleOwner, Observer{
                (rvCategoryItems.layoutManager as LinearLayoutManager)
                    .scrollToPositionWithOffset(it, 200)
            })


            itemList.observe(
                lifeCycleOwner,
                Observer { response ->
                    when (response) {
                        is Resource.Success -> {
                            response.data?.let { movie ->
                                holder.adapter.differ.submitList(movie)
                            }
                        }
                        is Resource.Loading -> {
                        }
                    }
                })


            holder.adapter.setOnItemClickListener {position, movieItem, imageView ->
//                startNavigation(holder.itemView, movieItem, imageView)
//                Handler(Looper.getMainLooper()).postDelayed({
//                    //doSomethingHere()
//                    scrollState.postValue(position)
//                }, 300)

                (activity as MainActivity).loadMovie()
            }
        }
    }

    fun startNavigation(view: View, movieItem: MovieItem, imageView: ImageView){

        val destination = HomeFragmentDirections.actionNavigationHomeToMovieDetailFragment(
            movieItem,
            imageView.transitionName,
            null,
            null
        )
        val extras = FragmentNavigatorExtras(imageView to imageView.transitionName)

        view.findNavController().navigate(
            destination,
            extras
        )
    }

    fun startHeroNavigation(view: View, heroItem: HeroItem, imageView: ImageView){

        val destination = HomeFragmentDirections.actionNavigationHomeToMovieDetailFragment(
            null,
            imageView.transitionName,
            null,
            heroItem
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