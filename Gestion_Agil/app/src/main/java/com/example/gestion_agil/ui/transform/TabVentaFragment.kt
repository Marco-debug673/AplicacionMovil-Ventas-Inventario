package com.example.gestion_agil.ui.transform

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gestion_agil.R
import com.example.gestion_agil.data.model.Ventas
import com.example.gestion_agil.databinding.FragmentTabVentaBinding
import com.example.gestion_agil.viewmodel.ProductosViewModel
import com.example.gestion_agil.viewmodel.VentasViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class TabVentaFragment : Fragment() {

    private var _binding: FragmentTabVentaBinding? = null
    private val binding get() = _binding!!

    private lateinit var ventasViewModel: VentasViewModel
    private lateinit var productosViewModel: ProductosViewModel

    private var precioActual = 0.0
    private var cantidadActual = 0
    private var totalActual = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTabVentaBinding.inflate(inflater, container, false)
        ventasViewModel = ViewModelProvider(this)[VentasViewModel::class.java]
        productosViewModel = ViewModelProvider(this)[ProductosViewModel::class.java]
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPaymentTypeSelector()

        // Buscar producto al escribir clave
        binding.claveProducto.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val clave = s.toString().trim()
                if (clave.isNotEmpty()) {
                    productosViewModel.buscarPorClave(clave)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Observar producto encontrado
        productosViewModel.productoEncontrado.observe(viewLifecycleOwner) { producto ->
            if (producto != null) {
                binding.nombreProducto.editText?.setText(producto.nombre_producto)
                binding.precioProducto.editText?.setText(producto.precio.toString())
                precioActual = producto.precio
                recalcularTotal()
            } else {
                binding.nombreProducto.editText?.setText("")
                binding.precioProducto.editText?.setText("")
                precioActual = 0.0
                recalcularTotal()
            }
        }

        // Escuchar cambios de cantidad
        binding.cantidadProducto.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                cantidadActual = s.toString().toIntOrNull() ?: 0
                recalcularTotal()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Observa resultado de inserción
        ventasViewModel.insertResult.observe(viewLifecycleOwner) { (success, message) ->
            AlertDialog.Builder(requireContext())
                .setTitle(if (success) "Venta registrada" else "Error")
                .setMessage(message)
                .setPositiveButton("Aceptar", null)
                .show()
            if (success) limpiarCampos()
        }

        binding.btnCobrar.setOnClickListener { mostrarDialogoCobro() }
    }

    private fun setupPaymentTypeSelector() {
        val paymentTypes = resources.getStringArray(R.array.tipos_pago)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, paymentTypes)
        (binding.tipoPago.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    @SuppressLint("SetTextI18n")
    private fun recalcularTotal() {
        totalActual = precioActual * cantidadActual
        val format = NumberFormat.getCurrencyInstance()
        binding.tvTotal.text = "Total: ${format.format(totalActual)}"
    }

    private fun mostrarDialogoCobro() {
        if (totalActual <= 0) {
            AlertDialog.Builder(requireContext())
                .setTitle("Sin datos")
                .setMessage("Asegúrate de ingresar un producto y cantidad válida")
                .setPositiveButton("Aceptar", null)
                .show()
            return
        }

        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 20, 50, 10)
        }

        val etPago = EditText(requireContext()).apply {
            hint = "Cantidad de pago"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        }

        val tvCambio = TextView(requireContext()).apply {
            text = "Cambio: $0.00"
            textSize = 16f
            setPadding(0, 15, 0, 0)
        }

        val etFecha = EditText(requireContext()).apply {
            isFocusable = false
            hint = "Seleccionar fecha"
            val fechaActual = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            setText(fechaActual)
            setOnClickListener {
                val cal = Calendar.getInstance()
                DatePickerDialog(requireContext(), { _, year, month, day ->
                    val fecha = String.format("%02d/%02d/%d", day, month + 1, year)
                    setText(fecha)
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        }

        layout.addView(etPago)
        layout.addView(tvCambio)
        layout.addView(etFecha)

        var cambioCalculado = 0.0

        etPago.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val pago = s.toString().toDoubleOrNull() ?: 0.0
                cambioCalculado = pago - totalActual
                val format = NumberFormat.getCurrencyInstance()
                tvCambio.text = "Cambio: ${format.format(if (cambioCalculado > 0) cambioCalculado else 0.0)}"
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        AlertDialog.Builder(requireContext())
            .setTitle("Cobrar venta")
            .setView(layout)
            .setPositiveButton("Guardar") { _, _ ->
                val pago = etPago.text.toString().toDoubleOrNull() ?: 0.0
                val fecha = etFecha.text.toString()

                if (pago <= 0) {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Pago incorrecto")
                        .setMessage("Por favor, ingresa la cantidad de pago válida.")
                        .setPositiveButton("Aceptar", null)
                        .show()
                    return@setPositiveButton
                }

                guardarVenta(pago, fecha, cambioCalculado)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun guardarVenta(pago: Double, fecha: String, cambio: Double) {
        val tipoPago = (binding.tipoPago.editText as? AutoCompleteTextView)?.text.toString()

        val venta = Ventas(
            metodo_pago = tipoPago,
            total_venta = totalActual,
            cantidad_pago = pago,
            cantidad_producto = cantidadActual,
            fecha_venta = fecha,
            cambio = if (cambio > 0) cambio else 0.0
        )

        ventasViewModel.insert(venta)

        // Mostrar confirmación con cambio
        val format = NumberFormat.getCurrencyInstance()
        val mensaje = """
        Venta guardada éxitosamente.
        
        Total: ${format.format(totalActual)}
        Pago: ${format.format(pago)}
        Cambio: ${format.format(if (cambio > 0) cambio else 0.0)}
    """.trimIndent()

        AlertDialog.Builder(requireContext())
            .setTitle("Venta registrada")
            .setMessage(mensaje)
            .setPositiveButton("Aceptar") { _, _ ->
                //limpiar solo después de cerrar el diálogo
                limpiarCampos()
            }
            .show()

        if (isAdded) limpiarCampos()
    }

    private fun limpiarCampos() {
        binding.claveProducto.editText?.setText("")
        binding.nombreProducto.editText?.setText("")
        binding.tipoPago.editText?.setText("")
        binding.cantidadProducto.editText?.setText("")
        binding.precioProducto.editText?.setText("")
        binding.tvTotal.text = getString(R.string.total_placeholder)
        precioActual = 0.0
        cantidadActual = 0
        totalActual = 0.0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}