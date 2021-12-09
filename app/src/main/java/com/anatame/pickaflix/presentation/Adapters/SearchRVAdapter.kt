package com.anatame.pickaflix.presentation.Adapters

import android.content.Context
import android.content.res.Resources.getSystem
import android.text.Layout
import android.transition.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.anatame.pickaflix.R
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.MovieItem
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.SearchMovieItem
import com.anatame.pickaflix.databinding.SearchItemMovie2Binding
import com.bumptech.glide.Glide

class SearchRVAdapter(
    val context: Context
) : RecyclerView.Adapter<SearchRVAdapter.SearchItemViewHolder>() {

    inner class SearchItemViewHolder(val binding: SearchItemMovie2Binding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<SearchMovieItem>() {
        override fun areItemsTheSame(oldItem: SearchMovieItem, newItem: SearchMovieItem): Boolean {
            return oldItem.thumbnailSrc == newItem.thumbnailSrc
        }

        override fun areContentsTheSame(oldItem: SearchMovieItem, newItem: SearchMovieItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
        val binding = SearchItemMovie2Binding.inflate(LayoutInflater.from(parent.context), parent, false)

        return SearchItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((SearchMovieItem, ImageView) -> Unit)? = null

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        val searchMovieItem = differ.currentList[position]
        ViewCompat.setTransitionName(holder.binding.ivMovieThumbnail, "iv$position")
        Log.d("searchResponse", searchMovieItem.toString())


        holder.itemView.apply {
            Glide.with(this)
                .load(searchMovieItem.thumbnailSrc)
                .centerCrop()
                .into(holder.binding.ivMovieThumbnail)
            holder.binding.apply {
                val layoutParams: LinearLayout.LayoutParams = card.layoutParams as LinearLayout.LayoutParams
                layoutParams.setMargins(16.px, 16.px, 0.px, 0.px)
                card.layoutParams = layoutParams

                tvMovieName.text = searchMovieItem.title
            }

            setOnClickListener {
                onItemClickListener?.let { it(
                    searchMovieItem,
                    holder.binding.ivMovieThumbnail
                )}
            }

        }
        setRotateAnimation(position, holder)

    }

    private var lastPosition = -1

    private fun setRotateAnimation(
        position: Int,
        holder: SearchRVAdapter.SearchItemViewHolder
    ) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.rv_scaleup);
            holder.itemView.startAnimation(animation)
            lastPosition = position
        }
    }
    //extension function to convert dp to px
   val Int.px: Int get() = (this * getSystem().displayMetrics.density).toInt()

    fun setOnItemClickListener(listener: (SearchMovieItem, ImageView) -> Unit) {
        onItemClickListener = listener
    }

}


