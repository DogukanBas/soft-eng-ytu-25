package com.example.mobile.ui.login
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.mobile.R
import com.example.mobile.ui.BaseFragment
import com.example.mobile.model.User.User
import com.example.mobile.model.User.UserType
import com.example.mobile.ui.accountant.AccountantMenuFragment
import com.example.mobile.ui.team_member.TeamMemberMenuFragment
import com.example.mobile.ui.admin.AdminMenuFragment
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var loginButton: Button


    companion object {
        const val TAG = "LoginFragment"
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

        observeUiState(stateFlow = viewModel.loginState,
            onSuccess = {
                Log.i(TAG, "Login successful for user: ${User.personalNo}")
                when (User.userType) {
                    UserType.ADMIN -> {
                        replaceFragment(AdminMenuFragment(), false)
                    }
                    UserType.MANAGER -> {


                    }
                    UserType.TEAM_MEMBER -> {
                        replaceFragment(
                            TeamMemberMenuFragment()
                        )

                        }
                        UserType.ACCOUNTANT -> {
                            replaceFragment(
                                AccountantMenuFragment()
                            )

                    }
                    null -> {
                        showToastError("User Type is Unknown")
                    }
                }
            },
            onError = {
                showToastError(it)
            })
    }
    private fun showToastError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

