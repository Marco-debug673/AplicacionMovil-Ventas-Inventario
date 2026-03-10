package com.example.gestion_agil.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gestion_agil.databinding.FragmentRegisterBinding
import com.example.gestion_agil.viewmodel.AuthViewModel

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel  by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observa los resultados de registro en tiempo real
        authViewModel.authResult.observe(viewLifecycleOwner) { (success, mensaje) ->
            if (success) {
                showAlertDialog("Registro Exitoso", mensaje) {
                    findNavController().navigateUp()
                }
            } else {
                showAlertDialog("Error de Registro", mensaje)
            }
        }

        binding.btnRegister.setOnClickListener {
            val nombre = binding.username.text.toString().trim()
            val correo = binding.email.text.toString().trim()
            val clave = binding.password.text.toString().trim()

            if (nombre.isEmpty() || correo.isEmpty() || clave.isEmpty()) {
                showAlertDialog("Campos Incompletos", "Por favor, completa todos los campos para continuar.")
                return@setOnClickListener
            }

            authViewModel.register(nombre, correo, clave)
        }

        binding.textGoToLogin.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showAlertDialog(titulo: String, mensaje: String, onConfirm: (() -> Unit)? = null) {
        AlertDialog.Builder(requireContext())
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
                onConfirm?.invoke()
            }
            .setCancelable(false)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}