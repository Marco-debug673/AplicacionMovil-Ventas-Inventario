package com.example.gestion_agil.ui.reflow

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gestion_agil.data.model.Productos
import com.example.gestion_agil.data.repository.SesionRepository
import com.example.gestion_agil.databinding.FragmentProductosBinding
import com.example.gestion_agil.utils.SecurityUtils
import com.example.gestion_agil.viewmodel.ProductosViewModel
import java.time.LocalDate
import java.util.Calendar
import java.time.format.DateTimeFormatter
import java.util.Locale

class ProductosFragment : Fragment() {

    private var _binding: FragmentProductosBinding? = null
    private val binding get() = _binding!!
    private lateinit var productosViewModel: ProductosViewModel
    private lateinit var sesionRepository: SesionRepository
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

        // Inicialización del ViewModel para gestionar la lógica de productos.
        productosViewModel = ViewModelProvider(this)[ProductosViewModel::class.java]

        // Inicialización del repositorio de sesión (SharedPreferences encriptadas)
        sesionRepository = SesionRepository(requireContext())

        // Cargar id_usuario desde SesionRepository
        // Esto soluciona el problema de "no se encontró la sesión" ya que el login guarda en Prefs, no en Room.
        val sesion = sesionRepository.obtenerSesion()
        idUsuario = sesion?.id_usuario ?: -1

        // Observar mensajes de error o validación desde el ViewModel
        productosViewModel.statusMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                mostrarDialogoResultado(false, it)
                productosViewModel.resetStatusMessage()
            }
        }

        binding.fechaCaducidadProducto.editText?.setOnClickListener {
            mostrarDatePicker()
        }

        // Guardar nuevo producto
        binding.btnGuardar.setOnClickListener { guardarProducto() }
    }

    // Función principal para capturar y validar los datos del nuevo producto.
    private fun guardarProducto() {
        val clave = binding.claveProducto.editText?.text.toString().trim()
        val nombre = binding.nombreProducto.editText?.text.toString().trim()
        val descripcion = binding.descripcionProducto.editText?.text.toString().trim()
        val precioStr = binding.precioProducto.editText?.text.toString().trim()
        val stockMinStr = binding.stockMinimoProducto.editText?.text.toString().trim()
        val stockMaxStr = binding.stockMaximoProducto.editText?.text.toString().trim()
        val activo = binding.switchActivoProducto.isChecked
        
        // Validación de campos vacíos
        if (clave.isEmpty() || nombre.isEmpty() || descripcion.isEmpty() ||
            precioStr.isEmpty() || stockMinStr.isEmpty() || stockMaxStr.isEmpty()
        ) {
            productosViewModel.showErrorMessage("Por favor, completa todos los campos")
            return
        }

        // Validación Whitelist para el nombre: Solo letras (incluyendo tildes y ñ), números y espacios
        val nombreRegex = Regex("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ ]+$")
        if (!nombre.matches(nombreRegex)) {
            productosViewModel.showErrorMessage("El nombre contiene caracteres no permitidos (solo letras, números y espacios)")
            return
        }

        val precio = precioStr.toDoubleOrNull() ?: 0.0
        val stockMin = stockMinStr.toIntOrNull() ?: 0
        val stockMax = stockMaxStr.toIntOrNull() ?: 0

        if (precio <= 0 || stockMin < 0 || stockMax <= 0) {
            productosViewModel.showErrorMessage("Valores numéricos incorrectos")
            return
        }

        // Validación de sesión
        if (idUsuario == -1) {
            productosViewModel.showErrorMessage("No se encontró la sesión del usuario")
            return
        }

        if (fechaSeleccionada.isEmpty()) {
            productosViewModel.showErrorMessage("Selecciona una fecha de caducidad")
            return
        }

        // Encriptar datos sensibles
        val claveEncriptada = SecurityUtils.encrypt(clave)
        val nombreEncriptado = SecurityUtils.encrypt(nombre)

        val producto = Productos(
            clave_producto = claveEncriptada,
            nombre_producto = nombreEncriptado,
            descripcion = descripcion,
            precio = precio,
            stock_minimo = stockMin,
            stock_maximo = stockMax,
            fecha_caducidad = fechaSeleccionada,
            activo = activo,
            idUsuarioForeign = idUsuario
        )

        // Insertar producto a través del ViewModel
        productosViewModel.insert(producto)
        
        // Mostrar confirmación y limpiar campos
        mostrarDialogoResultado(true, "Producto registrado correctamente")
        limpiarCampos()
    }

    // Gestión del selector de fecha para la caducidad del producto.
    private fun mostrarDatePicker() {
        val calendario = Calendar.getInstance()
        val año = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val fechaObjeto = LocalDate.of(year, month + 1, dayOfMonth)
                val formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale("es"))
                fechaSeleccionada = fechaObjeto.format(formatter)
                binding.fechaCaducidadProducto.editText?.setText(fechaSeleccionada)
            },
            año, mes, dia
        )
        datePicker.show()
    }

    // Muestra una alerta visual indicando si la operación fue exitosa o fallida.
    private fun mostrarDialogoResultado(success: Boolean, mensaje: String) {
        val titulo = if (success) "Éxito" else "Atención"
        val icon = if (success)
            android.R.drawable.checkbox_on_background
        else
            android.R.drawable.ic_dialog_alert

        AlertDialog.Builder(requireContext())
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("Aceptar", null)
            .setIcon(icon)
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
