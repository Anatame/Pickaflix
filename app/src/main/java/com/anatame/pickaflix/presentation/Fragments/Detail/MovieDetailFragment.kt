package com.anatame.pickaflix.presentation.Fragments.Detail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.*
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.navArgs
import com.anatame.pickaflix.databinding.FragmentMovieDetailBinding
import com.bumptech.glide.Glide
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.ChangeBounds
import androidx.transition.ChangeTransform
import androidx.transition.Transition
import androidx.transition.TransitionSet
import com.anatame.pickaflix.R
import com.anatame.pickaflix.common.Resource
import com.anatame.pickaflix.common.utils.BlockHosts
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.MovieDetails
import com.anatame.pickaflix.presentation.Adapters.ServerAdapter
import com.google.android.material.chip.Chip
import java.io.ByteArrayInputStream
import java.io.InputStream


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
                .into(binding.ivHero)
            viewModel.getMovieDetails(args.movie?.Url.toString())
        }

        if(args.searchMovieItem != null){
            Glide.with(this).load(args.searchMovieItem?.thumbnailSrc)
                .centerCrop()
                .into(binding.ivHero)

            // get movie details passing the src
            viewModel.getMovieDetails(args.searchMovieItem?.src.toString())
            Log.d("MOvieDetailUrl", args.searchMovieItem?.src.toString())
        }

        args.heroItem?.let {
            Glide.with(this).load(it.backgroundImageUrl)
                .centerCrop()
                .into(binding.ivHero)

            viewModel.getMovieDetails(it.source)
        }

        viewModel.movieDetails.observe(viewLifecycleOwner, Observer { response->
            when(response){
                is Resource.Success -> {
                    response.data?.let{
                        setContent(it)
                    }
                }
                is Resource.Loading -> {
                Toast.makeText(activity, "Loading", Toast.LENGTH_SHORT)
                    .show()
                }
            }
        })

        ViewCompat.setTransitionName(binding.ivHero, args.imageID)


        return binding.root
    }

    @SuppressLint("ResourceType")
    private fun setContent(movieDetails: MovieDetails){
        binding.apply {
            Glide.with(requireContext()).load(movieDetails.movieBackgroundCoverUrl)
                .centerCrop()
                .into(binding.ivHero)
            tvMovieName.text = movieDetails.movieTitle
            tvMovieLength.text = movieDetails.movieLength
            tvMovieRating.text = "IMDB: ${movieDetails.movieRating}"
            tvCaption.text = movieDetails.movieDescription
//            tvMovieCaption.text = movieDetails.movieDescription
            rvServers.apply {
                adapter = ServerAdapter(listOf("UpCloud", "VidCloud", "Hydrax"))
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )

            }
            var genreList: List<String> = movieDetails.genre.split(",").map { it.trim() }

            Log.d("genreList", movieDetails.genre)

            genreList.forEach { item ->
                val chip = Chip(requireContext())
                chip.text = item
//                chip.setTextAppearanceResource(R.style.ChipTextStyle_Selected)
//                chip.chipBackgroundColor =  ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.PrimaryAccent))
                cgGenre.addView(chip)
            }

            tvCast.text = "Cast: ${movieDetails.casts}"
            tvProduction.text = "Production: ${movieDetails.production}"

            wvPlayer.webViewClient = WebViewClient()
            wvPlayer.settings.javaScriptEnabled = true
            wvPlayer.settings.userAgentString = "Mozilla/5.0 (Linux; Android 7.0; SM-G930V Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.125 Mobile Safari/537.36"

            wvPlayer?.settings?.userAgentString = "Mozilla/5.0 (Linux; Android 7.0; SM-G930V Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.125 Mobile Safari/537.36"
            val map = HashMap<String, String>()
            map.put("referer", "https://fmoviesto.cc")

            wvPlayer.loadUrl(
                "https://streamrapid.ru/embed-4/FZbgGAE8iDRR?z=",
                map
            )
//            wvPlayer.loadUrl(
//                "https://embed2.megaxfer.ru/embed2/c9ffb234a4a622dbfdb7d7e319778330",
//                map
//            )


            wvPlayer.getSettings().setDomStorageEnabled(true);
            wvPlayer.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT)
            wvPlayer.getSettings().setAppCacheEnabled(true);
            wvPlayer.getSettings().setAppCachePath(requireContext().getFilesDir().getAbsolutePath() + "/cache");
            wvPlayer.getSettings().setDatabaseEnabled(true);
            wvPlayer.getSettings().setDatabasePath(requireContext().getFilesDir().getAbsolutePath() + "/databases");

            fun getTextWebResource(data: InputStream): WebResourceResponse {
                return WebResourceResponse("text/plain", "UTF-8", data);
            }

            wvPlayer.webViewClient = object : WebViewClient() {

                override fun shouldInterceptRequest(
                    view: WebView?,
                    request: WebResourceRequest?
                ): WebResourceResponse? {

                    if (BlockHosts().hosts.contains(request!!.url.host)) {
                        val textStream: InputStream = ByteArrayInputStream("".toByteArray())
                        return getTextWebResource(textStream)
                    }


                    return super.shouldInterceptRequest(view, request)
                }

            }



            ivBackBtn.setOnClickListener {
                findNavController().popBackStack()
            }
        }
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