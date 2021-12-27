package com.anatame.pickaflix.presentation.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anatame.pickaflix.databinding.DetailsRvItemBinding
import com.anatame.pickaflix.databinding.KingRvItemBinding

class DetailsRVKingAdapter(
    context: Context,
    val rv: RecyclerView
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class testRVHolder(
        val binding: KingRvItemBinding
    ):  RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val detailsKingRvItemBinding =  KingRvItemBinding
            .inflate(
                LayoutInflater
                    .from(parent.context), parent, false)
        return testRVHolder(detailsKingRvItemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder as testRVHolder
        holder.binding.testRV.apply{
            adapter = DetailsRVMasterAdapter( holder.binding.testRV, rv)
            layoutManager = LinearLayoutManager(context)
        }


    }

    override fun getItemCount(): Int {
        return 1
    }

}

