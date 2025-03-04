package com.example.findmovie.presentation.features.login

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.findmovie.R
import com.example.findmovie.databinding.FragmentLoginBinding
import com.example.findmovie.presentation.base.BaseFragment
import com.example.findmovie.data.remote.httpRequest.Resource
import com.example.findmovie.presentation.extension.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Login : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel: LoginViewModel by viewModels()

    override fun setUp() {
        super.setUp()
        observeViewModel()
    }

    override fun setListeners() {
        super.setListeners()
        binding.loginId.setOnClickListener {
            it.hideKeyboard()
            val email = binding.emailId.text.toString().trim()
            val password = binding.passwordId.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                binding.errorMessageId.visibility = View.VISIBLE
                binding.errorMessageId.text = getString(R.string.please_enter_email_and_password)
                return@setOnClickListener
            }
            viewModel.login(email, password)
        }
        binding.backArrowId.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginState.collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        with(binding) {
                            progressId.visibility = View.VISIBLE
                            errorMessageId.visibility = View.GONE
                            loginId.isEnabled = false
                        }
                    }
                    is Resource.Success -> {
                        navigateToHome()
                    }
                    is Resource.Error -> {
                        with(binding) {
                            progressId.visibility = View.GONE
                            loginId.isEnabled = true
                            errorMessageId.visibility = View.VISIBLE
                            errorMessageId.text = result.message
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun navigateToHome(){
        val action = LoginDirections.actionLogin2ToHomePage()
        findNavController().navigate(action)
    }

}