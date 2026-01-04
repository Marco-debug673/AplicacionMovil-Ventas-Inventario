package com.example.gestion_agil.ui.transform

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestion_agil.data.model.AppDatabase
import com.example.gestion_agil.data.model.Ventas
import com.example.gestion_agil.data.repository.VentasRepository
import com.example.gestion_agil.databinding.FragmentTabHistorialBinding
import com.example.gestion_agil.viewmodel.VentasViewModel

class TabHistorialFragment : Fragment() {

    private var _binding: FragmentTabHistorialBinding? = null
    private val binding get() = _binding!!

    private lateinit var historialViewModel: TabHistorialViewModel
    private lateinit var ventasViewModel: VentasViewModel
    private lateinit var historialAdapter: HistorialAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTabHistorialBinding.inflate(inflater, container, false)

        val application = requireActivity().application
        val ventasDao = AppDatabase.getDatabase(application).ventasDao()
        val repository = VentasRepository(ventasDao)
        val factory = TabHistorialViewModelFactory(repository)

        //Asegúrate de que TabHistorialViewModel esté importado del mismo paquete
        historialViewModel = ViewModelProvider(this, factory)[TabHistorialViewModel::class.java]

        ventasViewModel = ViewModelProvider(this)[VentasViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchView()
        observeHistorial()
        observeEliminarVenta()
    }

    private fun setupRecyclerView() {
        historialAdapter = HistorialAdapter(emptyList(), ventasViewModel)
        binding.recyclerViewHistorial.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historialAdapter
        }
    }

    private fun setupSearchView() {
        binding.searchViewHistorial.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                historialViewModel.onSearchQueryChanged(newText)
                return true
            }
        })
    }

    private fun observeHistorial() {
        historialViewModel.historial.observe(viewLifecycleOwner) { historialList ->
            historialAdapter.updateData(historialList)
        }
    }

    private fun observeEliminarVenta() {
        ventasViewModel.selectedVentaParaEliminar.observe(viewLifecycleOwner) { venta ->
            venta?.let {
                mostrarDialogoEliminar(it)
                ventasViewModel.selectVentaParaEliminar(null)
            }
        }
    }

    private fun mostrarDialogoEliminar(venta: Ventas) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar venta")
            .setMessage("¿Deseas eliminar la venta del ${venta.fecha_venta}?")
            .setPositiveButton("Sí") { _, _ ->
                ventasViewModel.delete(venta)
                Toast.makeText(requireContext(), "Venta eliminada correctamente", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}