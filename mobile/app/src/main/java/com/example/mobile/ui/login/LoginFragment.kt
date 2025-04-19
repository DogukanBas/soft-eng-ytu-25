package com.example.mobile.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import androidx.lifecycle.lifecycleScope
import com.example.mobile.MainActivity
import com.example.mobile.R
import com.example.mobile.model.User.User
import com.example.mobile.model.User.UserType
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
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        emailInput = view.findViewById(R.id.emailInput)
        passwordInput = view.findViewById(R.id.passwordInput)
        loginButton = view.findViewById(R.id.loginButton)

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
                    is LoginViewModel.LoginState.Loading -> {}
                    is LoginViewModel.LoginState.Success -> {
                        when (User.userType) {
                            UserType.ADMIN -> {
                                (activity as MainActivity).activateAdminNavigation()
                            }
                            UserType.MANAGER -> {
                                (activity as MainActivity).activateManagerNavigation()
                            }
                            UserType.TEAM_MEMBER -> {
                                (activity as MainActivity).activateTeamMemberNavigation()
                            }
                            UserType.ACCOUNTANT -> {
                                (activity as MainActivity).activateAccountantNavigation()
                            }

                            null -> {
                                showError("User Type is Unknown")
                            }
                        }
                    }
                    is LoginViewModel.LoginState.Error -> {
                        showError(state.message)
                    }
                }
            }
        }
        return view
    }

    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
