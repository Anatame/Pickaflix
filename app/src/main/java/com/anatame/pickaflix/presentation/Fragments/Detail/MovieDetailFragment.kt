package com.anatame.pickaflix.presentation.Fragments.Detail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.*
import android.widget.*
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.anatame.pickaflix.databinding.FragmentMovieDetailBinding
import com.bumptech.glide.Glide
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
import com.anatame.pickaflix.presentation.Fragments.home.HomeFragmentDirections
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.chip.Chip
import java.io.ByteArrayInputStream
import java.io.InputStream
import com.facebook.shimmer.ShimmerFrameLayout
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.content.ContextCompat.startActivity
import com.anatame.pickaflix.MainActivity
import com.anatame.pickaflix.presentation.PlayerActivity


class MovieDetailFragment : Fragment() {

    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var binding: FragmentMovieDetailBinding
    private lateinit var container: ShimmerFrameLayout
    private val args: MovieDetailFragmentArgs by navArgs()


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

        args.imageID?.let {
            ViewCompat.setTransitionName(binding.ivHero, args.imageID)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[MovieDetailViewModel::class.java]

        binding.ivBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        container = binding.shimmerViewContainer
        container.startShimmer()

        val begin = System.currentTimeMillis()

        if(args.movie != null){
            Glide.with(this).load(args.movie?.thumbnailUrl)
                .apply(
                    RequestOptions()
                    .placeholder(R.drawable.backgroundplaceholder)
                )
                .into(binding.ivHero)


            viewModel.getMovieDetails(args.movie?.Url.toString())

            if(args.movie?.movieType == "TV") {
                viewModel.getSeasons(args.movie?.Url.toString())
            }

            if(args.movie?.movieType == "Movie"){
                viewModel.getMovieData(args.movie?.Url.toString())
            }
        }

        if(args.searchMovieItem != null){
            Glide.with(this).load(args.searchMovieItem?.thumbnailSrc)
                .centerCrop()
                .into(binding.ivHero)

            // get movie details passing the src
            viewModel.getMovieDetails(args.searchMovieItem?.src.toString())

            if(args.searchMovieItem?.src!!.contains("tv")) {
                viewModel.getSeasons(args.searchMovieItem?.src.toString())
            }

            if(args.searchMovieItem?.src!!.contains("movie")) {
                viewModel.getMovieData(args.searchMovieItem?.src.toString())
            }

        }

        args.heroItem?.let {
            Glide.with(this).load(it.backgroundImageUrl)
                .centerCrop()
                .into(binding.ivHero)

            viewModel.getMovieDetails(it.source)

            if(args.heroItem?.source!!.contains("tv")) {
                viewModel.getSeasons(args.heroItem?.source.toString())
            }
            if(args.heroItem?.source!!.contains("movie")) {
                viewModel.getMovieData(args.heroItem?.source.toString())
            }

        }

        viewModel.movieDetails.observe(viewLifecycleOwner, Observer { response->
            when(response){
                is Resource.Success -> {
                    response.data?.let{

                        setContent(it)
                        // need to add "?playlist=$vidId&loop=1" to enable loop for youtube embed
                        val vidId = it.movieTrailerUrl.substring(30, it.movieTrailerUrl.length)
                        loadPlayer(it.movieTrailerUrl + "?playlist=$vidId&loop=1")
                    }
                }
                is Resource.Loading -> {
                    Toast.makeText(activity, "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
        viewModel.vidEmbedLink.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let{
                        Toast.makeText(context,
                            (System.currentTimeMillis() - begin).toString(),
                            Toast.LENGTH_SHORT)
                            .show()
                        loadEpsPlayer(it)
                    }
                }
            }
        })

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

        }
    }

    private fun loadEpsPlayer(vidEmbedURl: String = "https://streamrapid.ru/embed-4/FZbgGAE8iDRR?z="){
        binding.apply {
            epsPlayer.webViewClient = WebViewClient()
            epsPlayer.settings.javaScriptEnabled = true
            epsPlayer.settings.userAgentString =
                "Mozilla/5.0 (Linux; Android 7.0; SM-G930V Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.125 Mobile Safari/537.36"

            epsPlayer?.settings?.userAgentString = "Mozilla/5.0 (Linux; Android 7.0; SM-G930V Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.125 Mobile Safari/537.36"
            epsPlayer.settings.setDomStorageEnabled(true);
            epsPlayer.settings.cacheMode = WebSettings.LOAD_DEFAULT
            epsPlayer.settings.setAppCacheEnabled(true);
            epsPlayer.settings.setAppCachePath(requireContext().filesDir.absolutePath + "/cache");
            epsPlayer.settings.databaseEnabled = true;
            epsPlayer.settings.setDatabasePath(requireContext().filesDir.absolutePath + "/databases");
            epsPlayer.settings.mediaPlaybackRequiresUserGesture = false;

            val map = HashMap<String, String>()
            map.put("referer", "https://fmoviesto.cc")

            epsPlayer.loadUrl(
                vidEmbedURl,
                map
            )

            epsPlayer.addJavascriptInterface(
                WebAppInterface(requireContext(), vidEmbedURl), "Android")

            fun getTextWebResource(data: InputStream): WebResourceResponse {
                return WebResourceResponse("text/plain", "UTF-8", data);
            }

            epsPlayer.webViewClient = object : WebViewClient() {

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

                override fun onLoadResource(view: WebView?, url: String?) {
                    super.onLoadResource(view, url)
                    if(url!!.endsWith("playlist.m3u8")){
                        Log.d("movieSeasons", url)
                    }
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)

                    epsPlayer.loadUrl(
                        """javascript:(function f() {
                            let myInterval = setInterval(() => {
                            let fBtn = document.querySelector('.jw-icon-fullscreen');
                            if(fBtn != null || fBtn != 'undefined'){
                                  document.querySelector('.jw-icon-fullscreen').addEventListener('click', function() {
                                     document.querySelector('.jw-svg-icon-fullscreen-off').style.display = 'none';
                                     document.querySelector('.jw-svg-icon-fullscreen-on').style.display = 'block';
                                     Android.showToast();
                                 });
                                   clearInterval(myInterval);
                            }
                        }, 200);
                        
                          let overlayInterval = setInterval(() => {
                            let overlay = document.getElementById('overlay-center');
                            if(overlay != null || overlay != 'undefined'){
                              document.getElementById('overlay-center').remove();
                               clearInterval(overlayInterval);
                            }
                        }, 50);
                      })()""".trimIndent().trimMargin()
                    );
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)



                    // jw-svg-icon-fullscreen-off
                    // jw-svg-icon-fullscreen-on



                    container.hideShimmer()
                    llTitleContainer.visibility = View.GONE

                    epsPlayer.visibility = View.VISIBLE
                }

            }


        }
    }
    /** Instantiate the interface and set the context  */
    class WebAppInterface(
        private val mContext: Context,
        private val url: String
    ) {

        /** Show a toast from the web page  */
        @JavascriptInterface
        fun showToast() {
            Toast.makeText(mContext, "clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(mContext, PlayerActivity::class.java)
            intent.putExtra("vidUrl", url)
            mContext.startActivity(intent)
        }
    }




    private fun loadPlayer(vidEmbedURl: String = "https://streamrapid.ru/embed-4/FZbgGAE8iDRR?z="){
        binding.apply {
            wvPlayer.visibility = View.INVISIBLE
            wvPlayer.webViewClient = WebViewClient()
            wvPlayer.settings.javaScriptEnabled = true
            wvPlayer.settings.userAgentString =
                "Mozilla/5.0 (Linux; Android 7.0; SM-G930V Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.125 Mobile Safari/537.36"

            wvPlayer?.settings?.userAgentString = "Mozilla/5.0 (Linux; Android 7.0; SM-G930V Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.125 Mobile Safari/537.36"
            val map = HashMap<String, String>()
            map.put("referer", "https://fmoviesto.cc")

            wvPlayer.loadUrl(
                vidEmbedURl,
                map
            )

            wvPlayer.settings.setDomStorageEnabled(true);
            wvPlayer.settings.cacheMode = WebSettings.LOAD_DEFAULT
            wvPlayer.settings.setAppCacheEnabled(true);
            wvPlayer.settings.setAppCachePath(requireContext().filesDir.absolutePath + "/cache");
            wvPlayer.settings.databaseEnabled = true;
            wvPlayer.settings.setDatabasePath(requireContext().filesDir.absolutePath + "/databases");
            wvPlayer.settings.mediaPlaybackRequiresUserGesture = false;

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

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    wvPlayer.loadUrl(
                        """javascript:(function f() {
                        document.querySelector('.ytp-cued-thumbnail-overlay-image').click();
                        document.querySelector('.ytp-chrome-controls').style.display = 'none';
                        document.querySelector('.ytp-chrome-bottom').style.display = 'none';
                        document.querySelector('.ytp-pause-overlay').style.display = 'none';
                        document.querySelector('.ytp-show-cards-title').style.display = 'none';
                        this.interval = setInterval(() => {
                            document.querySelectorAll(".ytp-ce-element").forEach((el) => el.remove());
                        }, 100);
                      })()""".trimIndent().trimMargin()
                    );

                    wvPlayer.visibility = View.VISIBLE
                }

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