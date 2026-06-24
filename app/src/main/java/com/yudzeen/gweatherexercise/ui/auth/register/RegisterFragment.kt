package com.yudzeen.gweatherexercise.ui.auth.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yudzeen.gweatherexercise.common.Result
import com.yudzeen.gweatherexercise.databinding.FragmentRegisterBinding
import com.yudzeen.gweatherexercise.repository.auth.InvalidConfirmPasswordException
import com.yudzeen.gweatherexercise.repository.auth.InvalidEmailException
import com.yudzeen.gweatherexercise.repository.auth.InvalidPasswordException
import com.yudzeen.gweatherexercise.repository.auth.UserAlreadyExistsException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels()

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFields()
        observeRegisterResult()
    }

    private fun observeRegisterResult() {
        viewModel.registerResult.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> {
                    binding.loadingIndicator.visibility = View.GONE
                    when(it.exception) {
                        is UserAlreadyExistsException -> binding.emailTextFieldContainer.error = it.exception.message
                        is InvalidEmailException -> binding.emailTextFieldContainer.error = it.exception.message
                        is InvalidPasswordException -> binding.passwordTextFieldContainer.error = it.exception.message
                        is InvalidConfirmPasswordException -> binding.confirmPasswordTextFieldContainer.error = it.exception.message
                    }
                }
                Result.Loading -> binding.loadingIndicator.visibility = View.VISIBLE
                is Result.Success<String> -> {
                    binding.loadingIndicator.visibility = View.GONE
                    Toast.makeText(requireContext(), "Registration Successful.", Toast.LENGTH_LONG).show()
                    findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment(it.data))
                }
            }
        }
    }

    private fun setupFields() {
        binding.emailTextField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                binding.emailTextFieldContainer.error = null
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })

        binding.passwordTextField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                binding.passwordTextFieldContainer.error = null

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        binding.confirmPasswordTextField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                binding.confirmPasswordTextFieldContainer.error = null
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })

        binding.submitButton.setOnClickListener {
            viewModel.register(binding.emailTextField.text.toString(),
                binding.passwordTextField.text.toString(),
                binding.confirmPasswordTextField.text.toString())
        }
    }
}