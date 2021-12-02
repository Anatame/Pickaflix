package com.anatame.pickaflix.presentation.Fragments.movieDetail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.anatame.pickaflix.databinding.FragmentHomeBinding

class MovieDetailFragment : Fragment() {

    private lateinit var viewModel: MovieDetailViewModel
    private var binding: FragmentHomeBinding? = null
    val args: MovieDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val movieData = args.movie

        Toast.makeText(context, movieData.title, Toast.LENGTH_SHORT).show()

        viewModel = ViewModelProvider(this)[MovieDetailViewModel::class.java]

        binding = FragmentHomeBinding.inflate(inflater, container, false)



        return binding!!.root
    }

}