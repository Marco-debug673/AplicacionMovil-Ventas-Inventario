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

        //Observa el usuario actual y actualiza la UI
        viewModel.usuarioActual.observe(viewLifecycleOwner) { usuario ->
            if (usuario != null) {
                binding.profileName.text = usuario.nombre_usuario
                binding.correo.text = usuario.correo_electronico

                binding.logoutButton.setOnClickListener {
                    mostrarDialogoCierreSesion(usuario.nombre_usuario)
            }
        } else  {
                binding.profileName.text = ""
                binding.correo.text = ""
            }
    }

    viewModel.logoutResult.observe(viewLifecycleOwner) {
        result ->
        val (success, message) = result

        if (success) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            irALogin()
        }
    }

        binding.ayuda.setOnClickListener {
            mostrarAyuda()
        }
    }

    private fun mostrarAyuda() {
        val mensajeAyuda = """
            ¿Cómo usar la aplicación correctamente?
            
            1. Iniciar sesión o registrarse 
            Ingresa con tu correo y contraseña. Si eres nuevo, registra tu cuenta.
            
            2. Registrar productos 
            Agrega productos con nombre, cantidad y fecha de caducidad. 
            La apllicación usará esta fecha para enviarte recordatorios.
            
            3. Notificaciones de caducidad
            Recibirás avisos cuando un producto: 
            • Esté a 3 días de caducar o caduque hoy
            
            4. Registrar ventas
            Registra una venta escribiendo la clave del producto y la cantidad vendida.
            
            5. Perfil
            En esta sección puedes ver tu nombre de usuario, correo, las notificaciones de caducidad y cerrar sesión.
            
            6. Cerrar sesión
            Toca “Cerrar sesión” y confirma para salir de la cuenta.
         """.trimIndent()
         
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Ayuda")
            .setMessage(mensajeAyuda)
            .setPositiveButton("Entendido") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun mostrarDialogoCierreSesion(nombreUsuario: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Cerrar sesión")
            .setMessage("¿Deseas cerrar sesión?")
            .setIcon(R.drawable.ic_logout)
            .setPositiveButton("Sí") { _, _ ->
                viewModel.logout(nombreUsuario)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}