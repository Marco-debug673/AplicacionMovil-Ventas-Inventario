package com.example.gestion_agil.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gestion_agil.R
import com.example.gestion_agil.databinding.FragmentRecoveryPasswordBinding

class RecoveryPasswordFragment : Fragment(R.layout.fragment_recovery_password) {

    private var _binding: FragmentRecoveryPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentRecoveryPasswordBinding.bind(view)

        binding.btnSendRecovery.setOnClickListener {
            val email = binding.etRecoveryEmail.text.toString().trim()

            if (email.isEmpty()) {
                binding.etRecoveryEmail.error = "Ingresa tu correo con el que te registraste"
                return@setOnClickListener
            }
            Toast.makeText(requireContext(), "Enlace enviado a $email", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}