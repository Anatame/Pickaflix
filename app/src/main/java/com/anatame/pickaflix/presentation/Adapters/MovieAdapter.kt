package com.anatame.pickaflix.presentation.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.anatame.pickaflix.R
import com.anatame.pickaflix.common.Extras.CustomViews.px
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.HeroItem
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.MovieItem
import com.anatame.pickaflix.databinding.ItemHeroViewpagerHolderBinding
import com.anatame.pickaflix.databinding.ItemMovieBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.qualifiers.ApplicationContext

class MovieAdapter(
    val context: Context
) : RecyclerView.Adapter<MovieAdapter.MovieItemViewHolder>() {

    inner class MovieItemViewHolder(val binding: ItemMovieBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<MovieItem>() {
        override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
            return oldItem.Url == newItem.Url
        }

        override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MovieItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((View, MovieItem, ImageView) -> Unit)? = null

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        val MovieItem = differ.currentList[position]
        ViewCompat.setTransitionName(
            holder.binding.ivMovieThumnail,
            "iv$position ${MovieItem.Url}"
        )

        holder.itemView.apply {
            Glide.with(this)
                .load(MovieItem.thumbnailUrl)
                .centerCrop()
                .into(holder.binding.ivMovieThumnail)
            holder.binding.apply {
                // used for custom spacing in grid layout
//                val layoutParams: LinearLayout.LayoutParams =
//                    card.layoutParams as LinearLayout.LayoutParams
//                layoutParams.setMargins(8.px, 16.px, 0.px, 0.px)
//                card.layoutParams = layoutParams

                tvMovieName.text = MovieItem.title
                tvReleaseDate.text = MovieItem.releaseDate
                tvMovieLength.text = MovieItem.length
            }

            setOnClickListener {view ->
                onItemClickListener?.let { it(view, MovieItem, holder.binding.ivMovieThumnail) }
            }

        }


        setRotateAnimation(position, holder)

    }

    private var lastPosition = -1

    private fun setRotateAnimation(
        position: Int,
        holder: MovieItemViewHolder
    ) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.rv_scaleup);
            holder.itemView.startAnimation(animation);
            lastPosition = position
        }
    }

    fun setOnItemClickListener(listener: (View, MovieItem, ImageView) -> Unit) {
        onItemClickListener = listener
    }

}