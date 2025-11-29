package com.example.gestion_agil.ui.transform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestion_agil.data.model.AppDatabase
import com.example.gestion_agil.data.repository.VentasRepository
import com.example.gestion_agil.databinding.FragmentTabHistorialBinding

class TabHistorialFragment : Fragment() {

    private var _binding: FragmentTabHistorialBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: TabHistorialViewModel
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
        viewModel = ViewModelProvider(this, factory)[TabHistorialViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchView()
        observeHistorial()
    }

    private fun setupRecyclerView() {
        historialAdapter = HistorialAdapter(emptyList())
        binding.recyclerViewHistorial.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historialAdapter
        }
    }

    private fun setupSearchView() {
        binding.searchViewHistorial.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.onSearchQueryChanged(newText)
                return true
            }
        })
    }

    private fun observeHistorial() {
        viewModel.historial.observe(viewLifecycleOwner) { historialList ->
            historialAdapter.updateData(historialList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}