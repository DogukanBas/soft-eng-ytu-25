package com.example.mobile.ui.login
import com.example.mobile.utils.MenuItem
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
import com.example.mobile.MainActivity
import com.example.mobile.R
import com.example.mobile.model.User.User
import com.example.mobile.model.User.UserType
import com.example.mobile.ui.admin.AddUserFragment
import com.example.mobile.utils.UiState
import com.google.android.material.textfield.TextInputEditText
import com.token.uicomponents.ListMenuFragment.IListMenuItem
import com.token.uicomponents.ListMenuFragment.ListMenuFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var loginButton: Button


    private fun getLogo(): Int {
        return R.drawable.ic_launcher_foreground
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_login, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                    is UiState.Idle -> {}
                    is UiState.Loading -> {}
                    is UiState.Success -> {
                        Log.i("LoginFragment", "Login successful for user: ${User.personalNo}")

                        when (User.userType) {
                            UserType.ADMIN -> {
                                val menuItems = mutableListOf<IListMenuItem>()
                                //admin has two functionalities, add user, add department
                                menuItems.add(MenuItem(
                                    "Add User",
                                    { (activity as MainActivity).replaceFragment(AddUserFragment()) }
                                ))
                                menuItems.add(MenuItem(
                                    "Add Department",
                                    { (activity as MainActivity).replaceFragment(AddUserFragment()) }
                                ))
                                val menuFragment = ListMenuFragment.newInstance(menuItems,"Admin Menu",true,getLogo())
                                (activity as MainActivity).replaceFragment(menuFragment as Fragment)


                                //add fragment AdminHome

                            }
                            UserType.MANAGER -> {

                            }
                            UserType.TEAM_MEMBER -> {

                            }
                            UserType.ACCOUNTANT -> {

                            }

                            null -> {
                                showError("User Type is Unknown")
                            }
                        }
                    }
                    is UiState.Error -> {
                        showError(state.message)
                    }
                }
            }
        }

    }

    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
