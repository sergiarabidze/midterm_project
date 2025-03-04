package com.example.findmovie.presentation.features.register

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.findmovie.R
import com.example.findmovie.databinding.FragmentRegisterBinding
import com.example.findmovie.presentation.base.BaseFragment
import com.example.findmovie.data.remote.httpRequest.Resource
import com.example.findmovie.presentation.extension.hideKeyboard
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class Register : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun setUp() {
        super.setUp()
        val user = registerViewModel.isUserLoggedIn()
        if (user) {
            val action = RegisterDirections.actionRegisterToHomePage()
            findNavController().navigate(action)
        }
        observeViewModel()
    }

    override fun setListeners() {
        super.setListeners()
        binding.loginId.setOnClickListener {
            val action = RegisterDirections.actionRegisterToLogin2()
            findNavController().navigate(action)
        }

        binding.registerId.setOnClickListener {
            it.hideKeyboard()
            val email = binding.emailId.text.toString().trim()
            val password = binding.passwordId.text.toString().trim()
            val confirmPassword = binding.confPasswordId.text.toString().trim()
            val username = binding.fullNameId.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || username.isEmpty()) {
                with(binding) {
                    progressId.visibility = View.GONE
                    errorMessageId.visibility = View.VISIBLE
                    errorMessageId.text = getString(R.string.all_fields_are_required)
                }
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                with(binding) {
                    progressId.visibility = View.GONE
                    errorMessageId.visibility = View.VISIBLE
                    errorMessageId.text = getString(R.string.passwords_do_not_match)
                }
                return@setOnClickListener
            }

            registerViewModel.register(email, password, username)
        }

    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            registerViewModel.registerState.collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        with(binding) {
                            errorMessageId.visibility = View.GONE
                            registerId.isEnabled = false
                            progressId.visibility = View.VISIBLE
                        }
                    }

                    is Resource.Success -> {
                        navigateToHome()
                    }

                    is Resource.Error -> {
                        with(binding) {
                            progressId.visibility = View.GONE
                            errorMessageId.visibility = View.VISIBLE
                            errorMessageId.text = result.message
                        }
                    }
                    else -> {
                    }
                }
            }
        }
    }

    private fun navigateToHome() {
        val action = RegisterDirections.actionRegisterToHomePage()
        findNavController().navigate(action)
    }
}