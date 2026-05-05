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
import com.example.gestion_agil.R
import com.example.gestion_agil.data.model.Productos
import com.example.gestion_agil.data.repository.SesionRepository
import com.example.gestion_agil.databinding.FragmentHistorialinventarioBinding
import com.example.gestion_agil.utils.SecurityUtils
import com.example.gestion_agil.viewmodel.ProductosViewModel
import com.google.android.material.switchmaterial.SwitchMaterial

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

        // Obtener el ID del usuario actual para filtrar los productos
        val sesionRepository = SesionRepository(requireContext())
        val sesion = sesionRepository.obtenerSesion()
        val idUsuario = sesion?.id_usuario ?: -1

        if (idUsuario != -1) {
            productosViewModel.setUserId(idUsuario)
        }

        // Observa los productos filtrados por usuario desde el ViewModel
        productosViewModel.productsByUser.observe(viewLifecycleOwner) { productos ->
            listaProductos = productos
            adapter.updateData(productos)
        }

        // Observa mensajes de error o validación (evita repetición al navegar)
        productosViewModel.statusMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                AlertDialog.Builder(requireContext())
                    .setTitle("Atención")
                    .setMessage(it)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Aceptar", null)
                    .show()
                productosViewModel.resetStatusMessage()
            }
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

        // Filtro de búsqueda en tiempo real desencriptando para comparar
        binding.searchViewHistorial.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                val texto = newText.orEmpty().lowercase()
                val listaFiltrada = listaProductos.filter {
                    val nombreDec = SecurityUtils.decrypt(it.nombre_producto).lowercase()
                    val claveDec = SecurityUtils.decrypt(it.clave_producto).lowercase()
                    nombreDec.contains(texto) || claveDec.contains(texto)
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
        val context = requireContext()
        val inputPrecio = EditText(context).apply {
            hint = getString(R.string.precio_productos)
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            setText(producto.precio.toString())
        }

        // Usamos SwitchMaterial para que sea idéntico al del registro (ProductosFragment)
        val switchActivo = SwitchMaterial(context).apply {
            text = getString(R.string.activo)
            isChecked = producto.activo
            textSize = 16f
        }

        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            val padding = (24 * resources.displayMetrics.density).toInt()
            setPadding(padding, padding, padding, 0)
            addView(inputPrecio)
            
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = (16 * resources.displayMetrics.density).toInt()
            }
            switchActivo.layoutParams = params
            addView(switchActivo)
        }

        AlertDialog.Builder(context)
            .setTitle(getString(R.string.editar))
            .setView(layout)
            .setPositiveButton(getString(R.string.guardar)) { _, _ ->
                val nuevoPrecio = inputPrecio.text.toString().toDoubleOrNull()
                val nuevoActivo = switchActivo.isChecked

                if (nuevoPrecio == null) {
                    Toast.makeText(context, "Precio incorrecto", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val productoEditado = producto.copy(
                    precio = nuevoPrecio,
                    activo = nuevoActivo
                )

                productosViewModel.update(productoEditado)
                
                Toast.makeText(context, "Producto actualizado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Diálogo para confirmar eliminación
     */
    private fun mostrarDialogoEliminar(producto: Productos) {
        val nombreDesencriptado = SecurityUtils.decrypt(producto.nombre_producto)
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar producto")
            .setMessage("¿Deseas eliminar el producto $nombreDesencriptado?")
            .setPositiveButton("Sí") { _, _ ->
                productosViewModel.delete(producto)
                // Mensaje manual
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
