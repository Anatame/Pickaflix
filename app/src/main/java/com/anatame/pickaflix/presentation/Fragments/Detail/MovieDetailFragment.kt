package com.anatame.pickaflix.presentation.Fragments.Detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.anatame.pickaflix.databinding.FragmentMovieDetailBinding
import com.bumptech.glide.Glide

class MovieDetailFragment : Fragment() {

    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var binding: FragmentMovieDetailBinding
    val args: MovieDetailFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)

        Glide.with(this).load(args.movie.thumbnailUrl)
            .into(binding.ivMovieThumnail)
        ViewCompat.setTransitionName(binding.ivMovieThumnail, args.imageID)

        viewModel = ViewModelProvider(this)[MovieDetailViewModel::class.java]

        return binding.root
    }


}