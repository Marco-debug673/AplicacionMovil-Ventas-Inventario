package com.example.gestion_agil.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gestion_agil.R
import com.example.gestion_agil.databinding.FragmentLoginBinding
import com.example.gestion_agil.ui.MainActivity
import com.example.gestion_agil.viewmodel.AuthViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observa los resultados de registro en tiempo real
        authViewModel.authResult.observe(viewLifecycleOwner) { (success, mensaje) ->
            Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()

            if (success) {
                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }

        binding.btnLogin.setOnClickListener {
            val correo = binding.email.text.toString().trim()
            val clave = binding.password.text.toString().trim()

            if (correo.isEmpty() || clave.isEmpty()) {
                Toast.makeText(requireContext(), "Ingresa el correo y la contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authViewModel.login(correo, clave)
        }

        binding.textForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recoverypasswordFragment)
        }

        binding.textGoToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}