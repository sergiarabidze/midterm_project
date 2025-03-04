package com.example.findmovie.presentation.features.my_list

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findmovie.R
import com.example.findmovie.data.remote.httpRequest.Resource
import com.example.findmovie.databinding.FragmentMyListBinding
import com.example.findmovie.presentation.base.BaseFragment
import com.example.findmovie.presentation.extension.showSnackbar
import com.example.findmovie.presentation.helper.Genres
import com.example.findmovie.presentation.model.MoviesModel
import com.example.findmovie.presentation.recycler.GenreAdapter
import com.example.findmovie.presentation.recycler.MoviesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MyList : BaseFragment<FragmentMyListBinding>(FragmentMyListBinding::inflate) {
    private val viewModel: MyListViewModel by viewModels()
    private val moviesAdapter by lazy {
        MoviesAdapter {
            navigateToDetails(it)
        }
    }
    private val genresAdapter by lazy {
        GenreAdapter { clickedGenre ->
            val genreValue =
                if (clickedGenre.value == getString(R.string.all)) null else clickedGenre.value
            viewModel.selectGenre(genreValue)
        }
    }

    override fun setListeners() {
        super.setListeners()
        binding.backArrowId.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    override fun setUp() {
        super.setUp()
        setObservers()
    }

    override fun setRecycler() {
        super.setRecycler()
        binding.moviesRecyclerId.apply {
            adapter = moviesAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        binding.genresRecyclerId.apply {
            adapter = genresAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        genresAdapter.submitList(Genres.genres)
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.filteredMovies.collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {
                        moviesAdapter.submitList(resource.data)
                        hideLoading()
                    }

                    is Resource.Error -> {
                        showError(resource.message)
                        hideLoading()
                    }

                    is Resource.Loading -> showLoading()

                    is Resource.Idle -> hideLoading()

                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedGenre.collect { selectedGenre ->
                val updatedGenres = Genres.genres.map { genre ->
                    when {
                        genre.value == getString(R.string.all) -> genre.copy(isSelected = selectedGenre == null)
                        else -> genre.copy(isSelected = genre.value == selectedGenre)
                    }
                }
                genresAdapter.submitList(updatedGenres)
            }
        }
    }

    private fun navigateToDetails(movie: MoviesModel) {
        val action = MyListDirections.actionMyListToMovieDetails(movie)
        findNavController().navigate(action)
    }

    private fun showLoading() {
        with(binding) {
            moviesRecyclerId.visibility = View.GONE
            progressbar.visibility = View.VISIBLE
        }

    }

    private fun hideLoading() {
        with(binding) {
            moviesRecyclerId.visibility = View.VISIBLE
            progressbar.visibility = View.GONE
        }
    }

    private fun showError(message: String?) {
        showSnackbar( message ?: getString(R.string.an_error_occurred))
    }
}