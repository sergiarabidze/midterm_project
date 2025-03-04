package com.example.findmovie.presentation.features.home_page


import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.findmovie.R
import com.example.findmovie.presentation.base.BaseFragment
import com.example.findmovie.databinding.FragmentHomePageBinding
import com.example.findmovie.data.remote.httpRequest.Resource
import com.example.findmovie.presentation.extension.showSnackbar
import com.example.findmovie.presentation.model.MoviesModel
import com.example.findmovie.presentation.recycler.MoviesAdapter
import com.example.findmovie.presentation.recycler.MoviesPagerAdapter
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class HomePage : BaseFragment<FragmentHomePageBinding>(FragmentHomePageBinding::inflate) {
    private val viewModel: HomePageViewModel by viewModels()
    private val movieAdapter by lazy { MoviesAdapter { navigateToDetails(it) } }
    private val recommendedMovieAdapter by lazy { MoviesAdapter { navigateToDetails(it) } }
    private val popularMoviesAdapter by lazy { MoviesPagerAdapter { navigateToDetails(it) } }


    override fun setUp() {
        super.setUp()
        viewModel.fetchUserMovies()
        observe()
    }

    override fun setListeners() {
        super.setListeners()
        binding.profileNavId.setOnClickListener {
            val action = HomePageDirections.actionHomePageToProfile()
            findNavController().navigate(action)
        }
        binding.myListId.setOnClickListener {
            val action = HomePageDirections.actionHomePageToMyList()
            findNavController().navigate(action)
        }
        binding.recommendedMoviesId.setOnClickListener {
            val action = HomePageDirections.actionHomePageToRecommendedMovies()
            findNavController().navigate(action)
        }
        binding.searchBar.setOnClickListener {
            val action = HomePageDirections.actionHomePageToSearch()
            findNavController().navigate(action)
        }
    }

    override fun setRecycler() {
        super.setRecycler()
        binding.myListRecycler.adapter = movieAdapter
        binding.myListRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        binding.recommendedRecycler.adapter = recommendedMovieAdapter
        binding.recommendedRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.pagerId.adapter = popularMoviesAdapter
        binding.pagerId.offscreenPageLimit = 3

        binding.pagerId.setPageTransformer { page, position ->
            val scaleFactor = 0.85f + (1 - abs(position)) * 0.15f
            page.scaleY = scaleFactor
            page.alpha = 0.5f + (1 - abs(position)) * 0.5f
            val pageMarginPx = 40

            when {
                position < -1 -> {
                    page.translationX = -page.width.toFloat()
                }

                position <= 1 -> {
                    val offset = position * pageMarginPx
                    page.translationX = offset
                }

                else -> {
                    page.translationX = page.width.toFloat()
                }
            }
        }
    }



    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.recommendedMoviesState.collectLatest { resource ->
                handleListLoading(resource, binding.shimmerRecommended, binding.recommendedRecycler, recommendedMovieAdapter)
                }
            }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.popularMoviesState.collectLatest { resource ->
                handlePagerLoading(resource, binding.shimmerContainer, binding.pagerId, popularMoviesAdapter)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userNameState.collectLatest { resource ->
                when (resource) {
                    is Resource.Loading -> { binding.userUsername.text = getString(R.string.loading) }
                    is Resource.Error -> { binding.userUsername.text = getString(R.string.error_loading_username) }
                    Resource.Idle -> {}
                    is Resource.Success -> { binding.userUsername.text = resource.data }
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userMoviesState.collectLatest { resource ->
                handleListLoading(resource, binding.shimmerMyList, binding.myListRecycler, movieAdapter)
            }
        }
    }

    private fun handleListLoading(
        resource: Resource<List<MoviesModel>>,
        shimmer: ShimmerFrameLayout,
        recyclerView: RecyclerView,
        adapter: MoviesAdapter
    ) {
        when (resource) {
            is Resource.Loading -> {
                shimmer.visibility = View.VISIBLE
                shimmer.startShimmer()
                recyclerView.visibility = View.INVISIBLE
                when (recyclerView) {
                    binding.myListRecycler -> {
                        binding.myListText.visibility = View.GONE
                        binding.myListArrow.visibility = View.GONE
                    }
                    binding.recommendedRecycler -> {
                        binding.recommendedText.visibility = View.GONE
                        binding.recommendedArrow.visibility = View.GONE
                    }
                }

            }

            is Resource.Success -> {
                shimmer.stopShimmer()
                shimmer.visibility = View.GONE
                adapter.submitList(resource.data)
                val isEmpty = resource.data.isEmpty()
                recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
                when (recyclerView) {
                    binding.myListRecycler -> {
                        binding.myListText.visibility = if (isEmpty) View.GONE else View.VISIBLE
                        binding.myListArrow.visibility = if (isEmpty) View.GONE else View.VISIBLE
                    }
                    binding.recommendedRecycler -> {
                        binding.recommendedText.visibility = if (isEmpty) View.GONE else View.VISIBLE
                        binding.recommendedArrow.visibility = if (isEmpty) View.GONE else View.VISIBLE
                    }
                }
            }

            is Resource.Error -> {
                when (recyclerView) {
                    binding.myListRecycler -> {
                        binding.myListText.visibility = View.VISIBLE
                        binding.myListArrow.visibility = View.VISIBLE
                    }
                    binding.recommendedRecycler -> {
                        binding.recommendedText.visibility = View.VISIBLE
                        binding.recommendedArrow.visibility = View.VISIBLE
                        binding.emptyStaterecommended.visibility = View.VISIBLE
                        binding.recommendedRecycler.visibility = View.GONE
                    }
                }
                shimmer.stopShimmer()
                shimmer.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                showError(resource.message)
            }
            Resource.Idle -> {
                shimmer.stopShimmer()
            }
        }
    }


    private fun handlePagerLoading(
        resource: Resource<List<MoviesModel>>,
        shimmer: ShimmerFrameLayout,
        viewPager: ViewPager2,
        adapter: MoviesPagerAdapter
    ) {
        when (resource) {
            is Resource.Loading -> {
                shimmer.visibility = View.VISIBLE
                shimmer.startShimmer()
                viewPager.visibility = View.INVISIBLE
            }
            is Resource.Success -> {
                shimmer.stopShimmer()
                shimmer.visibility = View.GONE
                viewPager.visibility = View.VISIBLE
                adapter.submitList(resource.data)
            }
            is Resource.Error -> {
                shimmer.stopShimmer()
                shimmer.visibility = View.GONE
                viewPager.visibility = View.VISIBLE
                binding.pagerId.visibility = View.INVISIBLE
                binding.emptyState.visibility = View.VISIBLE
                showError(resource.message)
            }
            Resource.Idle -> {
                shimmer.stopShimmer()
            }
        }
    }

    private fun showError(message: String?) {
        showSnackbar( message ?: getString(R.string.an_error_occurred))
    }

    private fun navigateToDetails(movie: MoviesModel) {
        val action = HomePageDirections.actionHomePageToMovieDetails(movie)
        findNavController().navigate(action)
    }
}