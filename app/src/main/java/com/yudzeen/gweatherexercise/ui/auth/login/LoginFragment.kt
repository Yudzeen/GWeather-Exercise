package com.yudzeen.gweatherexercise.ui.auth.login

import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.yudzeen.gweatherexercise.R
import com.yudzeen.gweatherexercise.common.Result
import com.yudzeen.gweatherexercise.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()
    private val args: LoginFragmentArgs by navArgs()

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.email?.let { binding.emailTextField.setText(it) }
        setupSignUp()

        binding.signInButton.setOnClickListener {
            viewModel.login(binding.emailTextField.text.toString(), binding.passwordTextField.text.toString())
        }

        registerActiveSessionResultObserver()
        registerLoginResultObserver()

        viewModel.checkIfHasActiveSession()
    }

    private fun registerActiveSessionResultObserver() {
        viewModel.activeSessionResult.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Success -> navigateToWeatherScreen()
                else -> { }
            }
        }
    }

    private fun registerLoginResultObserver() {
        viewModel.loginResult.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> {
                    binding.loadingIndicator.visibility = View.GONE
                    Snackbar.make(binding.root, "Login Error.", Snackbar.LENGTH_LONG).show()
                }
                Result.Loading -> {
                    binding.loadingIndicator.visibility = View.VISIBLE
                }
                is Result.Success<Unit> -> {
                    binding.loadingIndicator.visibility = View.GONE
                    navigateToWeatherScreen()
                }
            }
        }
    }

    private fun navigateToWeatherScreen() {
        // TODO: Should be unable to back after login
    }

    private fun setupSignUp() {
        val signUpText = getString(R.string.sign_up)
        val fullText = getString(R.string.don_t_have_an_account) + " " + signUpText
        val spannableString = SpannableString(fullText)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                findNavController(p0).navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }

        spannableString.setSpan(
            clickableSpan,
            fullText.indexOf(signUpText),
            fullText.indexOf(signUpText) + signUpText.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.signUpText.apply {
            text = spannableString
            movementMethod = LinkMovementMethod.getInstance()
        }
    }
}