package com.anatame.pickaflix.presentation.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.HeroItem
import com.anatame.pickaflix.databinding.PagerHeroTemBinding
import android.content.Context
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.MovieItem
import com.bumptech.glide.Glide

class HeroPagerAdapter(
    val context: Context
) : RecyclerView.Adapter<HeroPagerAdapter.HeroPagerViewHolder>() {

    inner class HeroPagerViewHolder(val binding: PagerHeroTemBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<HeroItem>() {
        override fun areItemsTheSame(oldItem: HeroItem, newItem: HeroItem): Boolean {
            return oldItem.source == newItem.source
        }

        override fun areContentsTheSame(oldItem: HeroItem, newItem: HeroItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroPagerViewHolder {
        val binding = PagerHeroTemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return HeroPagerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((MovieItem, ImageView) -> Unit)? = null

    override fun onBindViewHolder(holder: HeroPagerViewHolder, position: Int) {
        val heroItem = differ.currentList[position]
    //    ViewCompat.setTransitionName(holder.binding.ivMovieThumnail, "iv$position")

        holder.itemView.apply {
            Glide.with(this)
                .load(heroItem.backgroundImageUrl)
                .centerCrop()
                .into(holder.binding.ivHero)
        }
    }




//    fun setOnItemClickListener(listener: (MovieItem, ImageView) -> Unit) {
//        onItemClickListener = listener
//    }

}
