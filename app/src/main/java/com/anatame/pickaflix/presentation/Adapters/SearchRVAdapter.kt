package com.anatame.pickaflix.presentation.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.MovieItem
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.SearchMovieItem
import com.anatame.pickaflix.databinding.ItemMovieBinding
import com.anatame.pickaflix.databinding.SearchItemMovieBinding
import com.bumptech.glide.Glide

class SearchRVAdapter : RecyclerView.Adapter<SearchRVAdapter.SearchItemViewHolder>() {

    inner class SearchItemViewHolder(val binding: SearchItemMovieBinding): RecyclerView.ViewHolder(binding.root)

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
        val binding = SearchItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return SearchItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((SearchMovieItem) -> Unit)? = null

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        val SearchMovieItem = differ.currentList[position]
        Log.d("searchResponse", SearchMovieItem.toString())

        holder.itemView.apply {
            Glide.with(this)
                .load(SearchMovieItem.thumbnailSrc)
                .into(holder.binding.ivMovieThumnail)
            holder.binding.apply {
                tvMovieName.text = SearchMovieItem.title
            }

            setOnClickListener {
                onItemClickListener?.let { it(SearchMovieItem) }
            }

        }
    }

    fun setOnItemClickListener(listener: (SearchMovieItem) -> Unit) {
        onItemClickListener = listener
    }

}


