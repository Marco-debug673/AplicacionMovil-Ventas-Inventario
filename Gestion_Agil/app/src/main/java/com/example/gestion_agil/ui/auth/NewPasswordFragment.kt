package com.example.gestion_agil.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.gestion_agil.R
import com.example.gestion_agil.data.model.HashUtils
import com.example.gestion_agil.databinding.FragmentNewPasswordBinding
import com.example.gestion_agil.viewmodel.RecoveryPasswordViewModel
import com.example.gestion_agil.viewmodel.RecoveryPasswordViewModelFactory
import kotlinx.coroutines.launch


class NewPasswordFragment : Fragment(R.layout.fragment_new_password) {

    private lateinit var binding: FragmentNewPasswordBinding

    private val viewModel: RecoveryPasswordViewModel by viewModels {
        RecoveryPasswordViewModelFactory(requireContext())
    }

    private var idUsuario: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentNewPasswordBinding.bind(view)
        idUsuario = arguments?.getInt("idUsuario") ?: -1

        binding.btnGuardarPassword.setOnClickListener {
            val pass1 = binding.edtNuevaPassword.text.toString()
            val pass2 = binding.edtConfirmarPassword.text.toString()

            if (pass1 != pass2) {
                binding.edtConfirmarPassword.error = "Las contraseñas no coinciden"
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val usuario = viewModel.buscarUsuarioPorId(idUsuario) ?: return@launch

                val nuevoSalt = HashUtils.generatesalt()
                val nuevaClave = HashUtils.hashwithsalt(pass1, nuevoSalt)

                viewModel.actualizarClave(usuario, nuevaClave, nuevoSalt)

                Toast.makeText(requireContext(), "Contraseña actualizada", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
    }
}