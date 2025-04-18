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
import com.example.mobile.model.User.User
import com.example.mobile.model.User.UserType
import com.example.mobile.viewmodels.LoginViewModel
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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

        lifecycleScope.launchWhenStarted {

            viewModel.loginState.collect { state ->
                when (state) {
                    is LoginViewModel.LoginState.Idle -> {}
                    is LoginViewModel.LoginState.Loading -> {

                    }
                    is LoginViewModel.LoginState.Success -> {
                        // Token'i kaydet ve ana sayfaya yönlendir
                        Log.i("LoginFragment", "Token: ${state.loginResponse.accessToken}")
                        User.setUser(
                            personalNo = state.loginResponse.personalNo,
                            email = state.loginResponse.email,
                            userType = UserType.fromString(state.loginResponse.userType),
                            accessToken = state.loginResponse.accessToken,

                        )
                        launch {

                            //viewModel.register("dogukan@gmail.com", "123", "21011001",UserType.TEAM_MEMBER)
                        }
                        viewModel.registerState.collect(){
                            when(it){
                                is LoginViewModel.RegisterState.Idle -> {
                                }
                                is LoginViewModel.RegisterState.Loading -> {
                                    Log.i("Sucess","register load")
                                }
                                is LoginViewModel.RegisterState.Success -> {
                                        Log.i("Sucess","register suces")
                                }
                                is LoginViewModel.RegisterState.Error -> {
                                    showError(it.message)
                                }
                            }
                        }

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



    private fun navigateToHome() {
        findNavController().navigate(R.id.action_loginFragment_to_navigation_home)
        // Ana sayfaya yönlendir
    }

    private fun showError(message: String) {
        // Snackbar veya Toast ile hata göster
    }
}
