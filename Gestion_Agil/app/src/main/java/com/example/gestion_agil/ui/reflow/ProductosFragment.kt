package com.example.gestion_agil.ui.reflow

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.gestion_agil.data.model.AppDatabase
import com.example.gestion_agil.data.model.Productos
import com.example.gestion_agil.databinding.FragmentProductosBinding
import com.example.gestion_agil.viewmodel.ProductosViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar
import java.time.format.DateTimeFormatter
import java.util.Locale

class ProductosFragment : Fragment() {

    private var _binding: FragmentProductosBinding? = null
    private val binding get() = _binding!!
    private lateinit var productosViewModel: ProductosViewModel
    private var idUsuario: Int = -1

    private var fechaSeleccionada: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productosViewModel = ViewModelProvider(this)[ProductosViewModel::class.java]

        val appDb = AppDatabase.getDatabase(requireContext())
        val sesionDao = appDb.SesionDao()

        // Cargar id_usuario desde Room
        lifecycleScope.launch {
            val sesion = sesionDao.getSesion()
            idUsuario = sesion?.id_usuario ?: -1
        }

        // Observar resultado de inserciones/actualizaciones/eliminaciones
        productosViewModel.insertResult.observe(viewLifecycleOwner) { (success, message) ->
            mostrarDialogoResultado(success, message)
            if (success) limpiarCampos()
        }

        binding.fechaCaducidadProducto.editText?.setOnClickListener {
            mostrarDatePicker()
        }

        // Guardar nuevo producto
        binding.btnGuardar.setOnClickListener { guardarProducto() }
    }

    private fun guardarProducto() {
        val clave = binding.claveProducto.editText?.text.toString().trim()
        val nombre = binding.nombreProducto.editText?.text.toString().trim()
        val descripcion = binding.descripcionProducto.editText?.text.toString().trim()
        val precioStr = binding.precioProducto.editText?.text.toString().trim()
        val stockMinStr = binding.stockMinimoProducto.editText?.text.toString().trim()
        val stockMaxStr = binding.stockMaximoProducto.editText?.text.toString().trim()
        val activo = binding.switchActivoProducto.isChecked

        if (clave.isEmpty() || nombre.isEmpty() || descripcion.isEmpty() ||
            precioStr.isEmpty() || stockMinStr.isEmpty() || stockMaxStr.isEmpty()
        ) {
            productosViewModel.showErrorMessage("Por favor, completa todos los campos")
            return
        }

        val precio = precioStr.toDoubleOrNull() ?: 0.0
        val stockMin = stockMinStr.toIntOrNull() ?: 0
        val stockMax = stockMaxStr.toIntOrNull() ?: 0

        if (precio <= 0 || stockMin < 0 || stockMax <= 0) {
            productosViewModel.showErrorMessage("Valores numéricos incorrectos")
            return
        }

        if (idUsuario == -1) {
            productosViewModel.showErrorMessage("No se encontró la sesión del usuario")
            return
        }

        if (fechaSeleccionada.isEmpty()) {
            productosViewModel.showErrorMessage("Selecciona una fecha de caducidad")
            return
        }

        val producto = Productos(
            clave_producto = clave,
            nombre_producto = nombre,
            descripcion = descripcion,
            precio = precio,
            stock_minimo = stockMin,
            stock_maximo = stockMax,
            fecha_caducidad = fechaSeleccionada,
            activo = activo,
            idUsuarioForeign = idUsuario
        )

        productosViewModel.insert(producto)
    }

    private fun mostrarDatePicker() {
        val calendario = Calendar.getInstance()
        val año = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                // 1. Crear un objeto LocalDate a partir de la selección del usuario.
                // Se suma 1 al mes porque en DatePickerDialog el rango es 0-11.
                val fechaObjeto = LocalDate.of(year, month + 1, dayOfMonth)

                val formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale("es"))
                fechaSeleccionada = fechaObjeto.format(formatter)
                binding.fechaCaducidadProducto.editText?.setText(fechaSeleccionada)
            },
            año, mes, dia
        )
        datePicker.show()
    }

    private fun mostrarDialogoResultado(success: Boolean, mensaje: String) {
        val titulo = if (success) "Éxito" else "Error"
        val icon = if (success)
            android.R.drawable.checkbox_on_background
        else
            android.R.drawable.ic_delete

        AlertDialog.Builder(requireContext())
            .setTitle(titulo)
            .setMessage(mensaje)
            .setIcon(icon)
            .setPositiveButton("Aceptar", null)
            .create()
            .show()
    }

    private fun limpiarCampos() {
        binding.claveProducto.editText?.setText("")
        binding.nombreProducto.editText?.setText("")
        binding.descripcionProducto.editText?.setText("")
        binding.precioProducto.editText?.setText("")
        binding.stockMinimoProducto.editText?.setText("")
        binding.stockMaximoProducto.editText?.setText("")
        binding.fechaCaducidadProducto.editText?.setText("")
        fechaSeleccionada = ""
        binding.switchActivoProducto.isChecked = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}