package com.example.findmovie.presentation.features.details

import android.content.Intent
import android.net.Uri
import android.util.Log.d
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findmovie.R
import com.example.findmovie.data.remote.httpRequest.Resource
import com.example.findmovie.presentation.base.BaseFragment
import com.example.findmovie.databinding.FragmentMovieDetailsBinding
import com.example.findmovie.presentation.model.DetailType
import com.example.findmovie.presentation.model.DetailsModel
import com.example.findmovie.presentation.extension.loadImage
import com.example.findmovie.presentation.extension.showSnackbar
import com.example.findmovie.presentation.recycler.ActorsAdapter
import com.example.findmovie.presentation.recycler.DetailsRecycler
import com.example.findmovie.presentation.recycler.GenreAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetails :
    BaseFragment<FragmentMovieDetailsBinding>(FragmentMovieDetailsBinding::inflate) {
    private val viewModel: MovieDetailsViewModel by viewModels()
    private val args: MovieDetailsArgs by navArgs()

    private val detailsAdapter by lazy {
        DetailsRecycler()
    }
    private val actorsAdapter by lazy {
        ActorsAdapter()
    }

    override fun setUp() {
        super.setUp()
        setObservers()
        viewModel.isMovieLiked(args.movie.id)

        val movie = args.movie

        with(binding) {
            titleId.text = movie.title
            descriptionId.text = movie.description
            posterId.loadImage(movie.poster)
            durationId.text = movie.duration
            descriptionId.text = movie.description
        }
    }

    override fun setRecycler() {
        super.setRecycler()
        with(binding) {
            genresRecyclerId.adapter = detailsAdapter
            genresRecyclerId.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            var details =
                args.movie.genres.map { DetailsModel(value = it, type = DetailType.GENRE) }
            details = details.plus(
                DetailsModel(
                    value = args.movie.rating.toString(),
                    type = DetailType.QUALITY
                )
            )
            detailsAdapter.submitList(details)

            actorsRecycler.adapter = actorsAdapter
            actorsRecycler.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            actorsAdapter.submitList(args.movie.actorModels)
        }
    }

    override fun setListeners() {
        super.setListeners()

        binding.heartId.setOnClickListener {
            if (binding.heartId.isChecked) {
                viewModel.addMovie(args.movie)
            } else {
                viewModel.removeMovie(args.movie)
            }
        }

        binding.imdbId.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(args.movie.trailer))
            startActivity(intent)
        }
        binding.playButtonId.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(args.movie.trailer))
            startActivity(intent)
        }

    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userMoviesState.collect {
                binding.heartId.isChecked = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.addMovieState.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        showSnackbar(getString(R.string.movie_added_to_favorites))
                    }
                    is Resource.Error -> {
                        showSnackbar(getString(R.string.failed_to_add_movie, resource.message))
                    }
                    else -> {}
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.removeMovieState.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        showSnackbar(getString(R.string.movie_removed_from_favorites))
                    }
                    is Resource.Error -> {
                        showSnackbar(getString(R.string.failed_to_remove_movie, resource.message))
                    }
                    else -> {}
                }
            }
        }
    }

}