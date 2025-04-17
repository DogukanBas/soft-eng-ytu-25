package com.example.mobile.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mobile.R
import com.example.mobile.viewmodels.LoginViewModel
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var loginButton: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate your layout here
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        emailInput = view.findViewById(R.id.emailInput)
        passwordInput = view.findViewById(R.id.passwordInput)
        loginButton = view.findViewById(R.id.loginButton)


        // Login butonuna tıklanınca
        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password)
            } else {
                Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
        // State'i observe et
        lifecycleScope.launchWhenStarted {
            viewModel.loginState.collect { state ->
                when (state) {
                    is LoginViewModel.LoginState.Idle -> {}
                    is LoginViewModel.LoginState.Loading -> {

                    }
                    is LoginViewModel.LoginState.Success -> {
                        // Token'i kaydet ve ana sayfaya yönlendir
                        saveToken(state.loginResponse.token)
                        Log.i("LoginFragment", "Token: ${state.loginResponse.token}")
                        navigateToHome()
                    }
                    is LoginViewModel.LoginState.Error -> {
                        showError(state.message)
                    }
                }
            }
        }

        return view
    }

    private fun saveToken(token: String) {
        Log.i("LoginFragment", "Token: $token")
        // SharedPreferences veya SecureStorage'a token'i kaydet
    }

    private fun navigateToHome() {
        findNavController().navigate(R.id.action_loginFragment_to_navigation_home)
        // Ana sayfaya yönlendir
    }

    private fun showError(message: String) {
        // Snackbar veya Toast ile hata göster
    }
}
