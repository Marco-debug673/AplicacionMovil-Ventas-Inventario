package com.example.gestion_agil.ui.reflow

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestion_agil.data.model.Productos
import com.example.gestion_agil.databinding.FragmentHistorialinventarioBinding
import com.example.gestion_agil.viewmodel.ProductosViewModel

class HistorialInventarioFragment : Fragment() {

    private var _binding: FragmentHistorialinventarioBinding? = null
    private val binding get() = _binding!!
    private val productosViewModel: ProductosViewModel by viewModels()
    private lateinit var adapter: ProductosAdapter
    private var listaProductos = listOf<Productos>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistorialinventarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar RecyclerView
        adapter = ProductosAdapter(emptyList(), productosViewModel)
        binding.recyclerViewHistorial.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewHistorial.adapter = adapter

        // Observa los productos desde el ViewModel (actualiza lista en tiempo real)
        productosViewModel.allProducts.observe(viewLifecycleOwner) { productos ->
            listaProductos = productos
            adapter.updateData(productos)
        }

        // Observa los resultados de operaciones (insert, update, delete)
        productosViewModel.insertResult.observe(viewLifecycleOwner) { (success, message) ->
            val icon = if (success)
                android.R.drawable.checkbox_on_background
            else
                android.R.drawable.ic_delete

            AlertDialog.Builder(requireContext())
                .setTitle(if (success) "Éxito" else "Error")
                .setMessage(message)
                .setIcon(icon)
                .setPositiveButton("Aceptar", null)
                .show()
        }

        // Observa los productos seleccionados para editar o eliminar
        productosViewModel.selectedProductoParaEditar.observe(viewLifecycleOwner) { producto ->
            producto?.let {
                mostrarDialogoEditar(it)
                productosViewModel.selectProductoParaEditar(null)
            }
        }

        productosViewModel.selectedProductoParaEliminar.observe(viewLifecycleOwner) { producto ->
            producto?.let {
                mostrarDialogoEliminar(it)
                productosViewModel.selectProductoParaEliminar(null)
            }
        }

        // Filtro de búsqueda en tiempo real
        binding.searchViewHistorial.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                val texto = newText.orEmpty().lowercase()
                val listaFiltrada = listaProductos.filter {
                    it.nombre_producto.lowercase().contains(texto) ||
                            it.clave_producto.lowercase().contains(texto)
                }
                adapter.updateData(listaFiltrada)
                return true
            }
        })
    }

    /**
     * Diálogo para editar el precio y estado del producto
     */
    private fun mostrarDialogoEditar(producto: Productos) {
        val inputPrecio = EditText(requireContext()).apply {
            hint = "Nuevo precio"
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            setText(producto.precio.toString())
        }

        val inputActivo = EditText(requireContext()).apply {
            hint = "Activo (true / false)"
            setText(producto.activo.toString())
        }

        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 30, 40, 0)
            addView(inputPrecio)
            addView(inputActivo)
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Editar producto")
            .setView(layout)
            .setPositiveButton("Guardar") { _, _ ->
                val nuevoPrecio = inputPrecio.text.toString().toDoubleOrNull()
                val nuevoActivo = inputActivo.text.toString().toBooleanStrictOrNull()

                if (nuevoPrecio == null || nuevoActivo == null) {
                    Toast.makeText(requireContext(), "Datos incorrectos", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val productoEditado = producto.copy(
                    precio = nuevoPrecio,
                    activo = nuevoActivo
                )

                productosViewModel.update(productoEditado)
                Toast.makeText(requireContext(), "Producto actualizado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Diálogo para confirmar eliminación
     */
    private fun mostrarDialogoEliminar(producto: Productos) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar producto")
            .setMessage("¿Deseas eliminar el producto ${producto.nombre_producto}?")
            .setPositiveButton("Sí") { _, _ ->
                productosViewModel.delete(producto)
                Toast.makeText(requireContext(), "Producto eliminado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}