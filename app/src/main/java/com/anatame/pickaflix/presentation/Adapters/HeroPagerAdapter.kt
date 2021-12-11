package com.anatame.pickaflix.presentation.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.HeroItem
import com.anatame.pickaflix.databinding.PagerHeroTemBinding
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.anatame.pickaflix.R
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.MovieItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

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

    private var onItemClickListener: ((Int, HeroItem, ImageView) -> Unit)? = null

    override fun onBindViewHolder(holder: HeroPagerViewHolder, position: Int) {
        val heroItem = differ.currentList[position]
    //    ViewCompat.setTransitionName(holder.binding.ivMovieThumnail, "iv$position")

        holder.itemView.apply {
            Glide.with(this)
                .load(heroItem.backgroundImageUrl)
                .centerCrop()
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                      return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.binding.shimmerViewContainer.hideShimmer()
                        return false
                    }

                })
                .into(holder.binding.ivHero)

            holder.binding.apply {
                tvMovieName.text = heroItem.title
                tvMovieLength.text = heroItem.duration
                tvMovieRating.text = "IMBD: ${heroItem.rating}"
                ViewCompat.setTransitionName(
                    ivHero,
                    "iv$position ${heroItem.source}"
                )
            }

            setOnClickListener {view ->
                onItemClickListener?.let { it(position, heroItem, holder.binding.ivHero) }
            }
        }

    }

    fun setOnItemClickListener(listener: (Int, HeroItem, ImageView) -> Unit) {
        onItemClickListener = listener
    }


//    fun setOnItemClickListener(listener: (MovieItem, ImageView) -> Unit) {
//        onItemClickListener = listener
//    }

}
