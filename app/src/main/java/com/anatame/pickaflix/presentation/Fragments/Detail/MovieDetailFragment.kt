package com.anatame.pickaflix.presentation.Fragments.Detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.navArgs
import com.anatame.pickaflix.databinding.FragmentMovieDetailBinding
import com.bumptech.glide.Glide
import android.widget.TextView
import androidx.transition.*


class MovieDetailFragment : Fragment() {

    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var binding: FragmentMovieDetailBinding
    val args: MovieDetailFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val transition: Transition = TransitionSet()
            .addTransition(ChangeTransform())
            .addTransition(ChangeBounds()) // For both

        sharedElementEnterTransition = transition

        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)

        if(args.movie != null){
            Glide.with(this).load(args.movie?.thumbnailUrl)
                .centerCrop()
                .into(binding.ivMovieThumnail)
        }

        if(args.searchMovieItem != null){
            Glide.with(this).load(args.searchMovieItem?.thumbnailSrc)
                .centerCrop()
                .into(binding.ivMovieThumnail)
        }


        ViewCompat.setTransitionName(binding.ivMovieThumnail, args.imageID)

        viewModel = ViewModelProvider(this)[MovieDetailViewModel::class.java]

        return binding.root
    }


}


// shared element tranistions specific to views

//val transition: Transition = TransitionSet()
//    .addTransition(ChangeTransform()).addTarget(TextView::class.java) // Only for TextViews
//    .addTransition(ChangeImageTransform())
//    .addTarget(ImageView::class.java) // Only for ImageViews
//    .addTransition(ChangeBounds()) // For both
//
//
//sharedElementEnterTransition = transition