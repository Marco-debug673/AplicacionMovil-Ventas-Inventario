package com.example.gestion_agil.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.gestion_agil.R
import com.example.gestion_agil.databinding.FragmentInputPinBinding
import com.example.gestion_agil.viewmodel.RecoveryPasswordViewModel
import com.example.gestion_agil.viewmodel.RecoveryPasswordViewModelFactory
import kotlinx.coroutines.launch

class InputPinFragment : Fragment(R.layout.fragment_input_pin) {

    private lateinit var binding: FragmentInputPinBinding

    private val viewModel: RecoveryPasswordViewModel by viewModels {
        RecoveryPasswordViewModelFactory(requireContext())
    }

    private lateinit var correo: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentInputPinBinding.bind(view)

        correo = arguments?.getString("email") ?: ""

        binding.btnVerificarPin.setOnClickListener {
            val pin = binding.edtPin.text.toString().trim()

            lifecycleScope.launch {
                val usuario = viewModel.validarPin(correo, pin)

                if (usuario == null) {
                    binding.edtPin.error = "PIN incorrecto"
                    return@launch
                }

                val action = InputPinFragmentDirections
                    .actionPinToNewpass(usuario.id_usuario)

                findNavController().navigate(action)
            }
        }
    }
}