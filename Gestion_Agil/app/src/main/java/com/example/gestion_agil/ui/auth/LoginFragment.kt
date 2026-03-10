package com.example.gestion_agil.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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

        // Observa los resultados de autenticación en tiempo real
        authViewModel.authResult.observe(viewLifecycleOwner) { (success, mensaje) ->
            if (success) {
                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            } else {
                showAlertDialog("Error de Inicio de Sesión", mensaje)
            }
        }

        binding.btnLogin.setOnClickListener {
            val correo = binding.email.text.toString().trim()
            val clave = binding.password.text.toString().trim()

            if (correo.isEmpty() || clave.isEmpty()) {
                showAlertDialog("Campos Incompletos", "Ingresa el correo y la contraseña para continuar.")
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

    private fun showAlertDialog(titulo: String, mensaje: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}