package com.example.findmovie.presentation.features.search

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.findmovie.R
import com.example.findmovie.data.remote.httpRequest.Resource
import com.example.findmovie.databinding.FragmentSearchBinding
import com.example.findmovie.presentation.base.BaseFragment
import com.example.findmovie.presentation.extension.showSnackbar
import com.example.findmovie.presentation.model.MoviesModel
import com.example.findmovie.presentation.recycler.MoviesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class Search : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    private val viewModel: SearchViewModel by viewModels()
    private val searchAdapter by lazy { MoviesAdapter { navigateToDetails(it) } }

    override fun setUp() {
        super.setUp()
        binding.searchBar.post {
            binding.searchBar.requestFocus()
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.searchBar, InputMethodManager.SHOW_IMPLICIT)
        }
        setupSearchBar()
        observeSearchResults()
    }

    private fun setupSearchBar() {
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.toString()?.let { query ->
                    viewModel.searchMovies(query)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun setListeners() {
        super.setListeners()
        binding.backArrowId.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun setRecycler() {
        super.setRecycler()
        binding.searchRecyclerId.apply {
            adapter = searchAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun observeSearchResults() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchResults.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        binding.emptyState.root.visibility = if (resource.data.isEmpty()) View.VISIBLE else View.GONE
                        binding.searchRecyclerId.visibility = if (resource.data.isEmpty()) View.GONE else View.VISIBLE
                        searchAdapter.submitList(resource.data)
                        hideLoading()
                    }
                    is Resource.Error -> {
                        binding.emptyState.root.visibility = View.VISIBLE
                        binding.searchRecyclerId.visibility = View.GONE
                        showError(resource.message)
                        hideLoading()
                    }
                    Resource.Loading -> {
                        showLoading()
                    }
                    Resource.Idle -> {
                        hideLoading()
                    }
                }
            }
        }
    }

    private fun showError(message: String?) {
        showSnackbar( message ?: getString(R.string.an_error_occurred))
    }

    private fun showLoading() {
        with(binding) {
            searchRecyclerId.visibility = View.GONE
            progressbar.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        with(binding) {
            searchRecyclerId.visibility = View.VISIBLE
            progressbar.visibility = View.GONE
        }
    }

    private fun navigateToDetails(movie: MoviesModel) {
        val action = SearchDirections.actionSearchToMovieDetails(movie)
        findNavController().navigate(action)
    }
}
