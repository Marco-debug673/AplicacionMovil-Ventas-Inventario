package com.example.gestion_agil.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gestion_agil.R
import com.example.gestion_agil.databinding.FragmentPerfilBinding
import com.example.gestion_agil.ui.LoginActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class PerfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PerfilViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[PerfilViewModel::class.java]

        // Mostrar datos del usuario Firebase
        viewModel.usuarioFirebase.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.profileName.text = user.displayName ?: "Usuario"
                binding.correo.text = user.email ?: ""
            }
        }

        binding.logoutButton.setOnClickListener {
            mostrarDialogoCierreSesion()
        }

        viewModel.logoutResult.observe(viewLifecycleOwner) { result ->
            if (result.first) {
                Toast.makeText(requireContext(), result.second, Toast.LENGTH_SHORT).show()
                irALogin()
            }
        }

        binding.ayuda.setOnClickListener {
            mostrarAyuda()
        }
    }

    private fun mostrarDialogoCierreSesion() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Cerrar sesión")
            .setMessage("¿Deseas cerrar sesión?")
            .setIcon(R.drawable.ic_logout)
            .setPositiveButton("Sí") { _, _ ->
                viewModel.logout()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun irALogin() {
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    private fun mostrarAyuda() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Ayuda")
            .setMessage(
                """
                ¿Cómo usar la aplicación?
                
                1. Crear una cuenta.
                2. Inicia sesión con tu cuenta.
                3. Registra productos.
                4. Recibe notificaciones de caducidad.
                5. Consulta tu perfil.
                6. Cierra sesión cuando lo desees.
                """.trimIndent()
            )
            .setPositiveButton("Entendido", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}