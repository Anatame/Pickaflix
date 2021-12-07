package com.anatame.pickaflix.presentation.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anatame.pickaflix.common.Resource
import com.anatame.pickaflix.domain.models.HomeItem
import com.anatame.pickaflix.databinding.ItemHeroViewpagerHolderBinding
import com.anatame.pickaflix.databinding.ItemHomeCategoryBinding

class HomeRVAdapter(
    val context: Context,
    val lifeCycleOwner: LifecycleOwner,
    val homeItemList: List<HomeItem>
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
            else -> 3
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
        Toast.makeText(context, "home size $itemCount", Toast.LENGTH_SHORT).show()
        when(holder.itemViewType){
            0 -> {
                holder as ViewPagerViewHolder
                val adapter = HeroPagerAdapter(context)
                holder.pagerBinding.vpHeroPager.adapter = adapter
                homeItemList[position].pagerList!!.observe(lifeCycleOwner, Observer { response ->
                    when(response) {
                        is Resource.Success -> {
                            response.data?.let { movie ->
                                Toast.makeText(context, "Finished", Toast.LENGTH_SHORT)
                                    .show()
                                adapter.differ.submitList(movie)
                            }
                        }
                        is Resource.Loading -> {
                            Toast.makeText(context, "Loading", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                })

            }

            1 -> {
                holder as CategoryViewHolder
                holder.rvItemBinding.apply {
                    tvCategoryName.text = "Trending Movies"

                    val adapter = MovieAdapter(context)
                    rvCategoryItems.adapter = adapter
                    rvCategoryItems.layoutManager = LinearLayoutManager(
                        context,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )

                    homeItemList[position].categoryItem!!.
                    movieItemList.observe(lifeCycleOwner, Observer { response ->
                            when(response) {
                                is Resource.Success -> {
                                    response.data?.let { movie ->
                                        adapter.differ.submitList(movie)
                                    }
                                }
                                is Resource.Loading -> {
                                }
                            }
                        })
                }
            }
            2 -> {
                holder as CategoryViewHolder
                holder.rvItemBinding.apply {
                    tvCategoryName.text = "Trending Shows"

                    val adapter = MovieAdapter(context)
                    rvCategoryItems.adapter = adapter
                    rvCategoryItems.layoutManager = LinearLayoutManager(
                        context,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )

                    homeItemList[position].categoryItem!!.
                    trendingShows.observe(lifeCycleOwner, Observer { response ->
                        when(response) {
                            is Resource.Success -> {
                                response.data?.let { movie ->
                                    adapter.differ.submitList(movie)
                                }
                            }
                            is Resource.Loading -> {
                            }
                        }
                    })
                }
            }
            3 -> {
                holder as CategoryViewHolder
                holder.rvItemBinding.apply {
                    tvCategoryName.text = "Latest Movies"

                    val adapter = MovieAdapter(context)
                    rvCategoryItems.adapter = adapter
                    rvCategoryItems.layoutManager = LinearLayoutManager(
                        context,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )

                    homeItemList[position].categoryItem!!.
                    latestMovies.observe(lifeCycleOwner, Observer { response ->
                        when(response) {
                            is Resource.Success -> {
                                response.data?.let { movie ->
                                    adapter.differ.submitList(movie)
                                }
                            }
                            is Resource.Loading -> {
                            }
                        }
                    })
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return homeItemList.size
    }

}