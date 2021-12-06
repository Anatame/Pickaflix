package com.anatame.pickaflix.presentation.Fragments.Detail

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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

        hideKeyboard()

        val transition: Transition = TransitionSet()
            .addTransition(ChangeTransform())
            .addTransition(ChangeBounds()) // For both

        sharedElementEnterTransition = transition

        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[MovieDetailViewModel::class.java]

        if(args.movie != null){
            Glide.with(this).load(args.movie?.thumbnailUrl)
                .centerCrop()
                .into(binding.ivMovieThumnail)
            viewModel.getMovieDetails(args.movie?.Url.toString())
        }

        if(args.searchMovieItem != null){
            Glide.with(this).load(args.searchMovieItem?.thumbnailSrc)
                .centerCrop()
                .into(binding.ivMovieThumnail)

            // get movie details passing the src
            viewModel.getMovieDetails(args.searchMovieItem?.src.toString())
            Log.d("MOvieDetailUrl", args.searchMovieItem?.src.toString())
        }


        ViewCompat.setTransitionName(binding.ivMovieThumnail, args.imageID)



        return binding.root
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        // To get the correct window token, lets first get the currently focused view
        var view = activity?.currentFocus;

        // To get the window token when there is no currently focused view, we have a to create a view
        if (view == null) {
            view = View(activity);
        }

        // hide the keyboard
        imm.hideSoftInputFromWindow(view.windowToken, 0);
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