package com.anatame.pickaflix.presentation.Adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.RecyclerView
import com.anatame.pickaflix.databinding.DetailsRvItemBinding
import com.anatame.pickaflix.databinding.ItemHeroViewpagerHolderBinding

class DetailsRVMasterAdapter(
    itemRv: RecyclerView,
    val parentRv: RecyclerView
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class testItems(
        val detailItemBinding: DetailsRvItemBinding
    ):  RecyclerView.ViewHolder(detailItemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val detailItemBinding =  DetailsRvItemBinding
            .inflate(
                LayoutInflater
                .from(parent.context), parent, false)
        return testItems(detailItemBinding)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder.itemView.setOnClickListener{
            Log.d("rvClickTest", "rv item clicked at pos $position")
        }


        holder.itemView.setOnTouchListener(View.OnTouchListener { v, event ->
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_UP) {
                    parentRv.requestDisallowInterceptTouchEvent(false)
                    Log.d("rvClickTest", "rv item clicked at pos $position false")
                } else {

                    parentRv.requestDisallowInterceptTouchEvent(true)
                }
            true
            })
    }

    override fun getItemCount(): Int {
        return 100
    }

}

