package com.example.findmovie.presentation.features.profile

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.findmovie.R
import com.example.findmovie.data.remote.httpRequest.Resource
import com.example.findmovie.databinding.FragmentProfileBinding
import com.example.findmovie.presentation.base.BaseFragment
import com.example.findmovie.presentation.extension.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class Profile : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val viewModel: ProfileViewModel by viewModels()

    override fun setUp() {
        super.setUp()
        setObservers()
    }

    override fun setListeners() {
        super.setListeners()
        binding.backArrowId.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.logoutButton.setOnClickListener {
            logOut()
        }
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.username.collect { resource ->
                when (resource) {
                    is Resource.Success -> binding.userName.text = resource.data
                    is Resource.Error -> binding.userName.text = showSnackbar(
                        getString(
                            R.string.errorr,
                            resource.message
                        )
                    ).toString()

                    else -> binding.userName.text = getString(R.string.loading)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.email.collect { resource ->
                when (resource) {
                    is Resource.Success -> binding.userEmail.text = resource.data
                    is Resource.Error -> showSnackbar(getString(R.string.error, resource.message))
                    else -> binding.userEmail.text = getString(R.string.loading)
                }
            }
        }
    }


    private fun logOut() {
        viewModel.logOut()
        findNavController().navigate(R.id.action_global_register)
    }


}
