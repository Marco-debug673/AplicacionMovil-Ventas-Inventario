package com.example.gestion_agil.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.gestion_agil.R
import com.example.gestion_agil.databinding.FragmentRecoveryPasswordBinding
import com.example.gestion_agil.viewmodel.RecoveryPasswordViewModel
import com.example.gestion_agil.viewmodel.RecoveryPasswordViewModelFactory
import kotlinx.coroutines.launch
import kotlin.getValue

class RecoveryPasswordFragment : Fragment(R.layout.fragment_recovery_password) {

    private var _binding: FragmentRecoveryPasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecoveryPasswordViewModel by viewModels {
        RecoveryPasswordViewModelFactory(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRecoveryPasswordBinding.bind(view)

        binding.btnSendRecovery.setOnClickListener {
            val email = binding.etRecoveryEmail.text.toString().trim()

            if (email.isEmpty()) {
                binding.etRecoveryEmail.error = "Ingresa tu correo"
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val usuario = viewModel.buscarUsuarioPorCorreo(email)

                if (usuario == null) {
                    Toast.makeText(requireContext(), "Este correo no está registrado", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val pin = (100000..999999).random().toString()
                viewModel.guardarPin(usuario, pin)

                Toast.makeText(requireContext(), "PIN generado: $pin", Toast.LENGTH_LONG).show()

                val action = RecoveryPasswordFragmentDirections
                    .actionRecoveryToPin(email)

                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}