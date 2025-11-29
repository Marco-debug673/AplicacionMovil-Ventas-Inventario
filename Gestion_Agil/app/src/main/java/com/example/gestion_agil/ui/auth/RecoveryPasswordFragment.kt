package com.example.gestion_agil.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.gestion_agil.R
import com.example.gestion_agil.databinding.FragmentRecoveryPasswordBinding
import com.example.gestion_agil.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

class RecoveryPasswordFragment : Fragment(R.layout.fragment_recovery_password) {

    private var _binding: FragmentRecoveryPasswordBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentRecoveryPasswordBinding.bind(view)

        binding.btnSendRecovery.setOnClickListener {
            val email = binding.etRecoveryEmail.text.toString().trim()
            authViewModel.recoverPassword(email)

            authViewModel.authResult.observe(viewLifecycleOwner) { result ->
                Toast.makeText(requireContext(), result.second, Toast.LENGTH_LONG).show()
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.tilRecoveryEmail.error = "Correo no válido"
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                binding.etRecoveryEmail.error = "Ingresa tu correo con el que te registraste"
                return@setOnClickListener
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}