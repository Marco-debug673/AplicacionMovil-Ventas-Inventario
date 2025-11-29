package com.example.gestion_agil.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestion_agil.data.model.AppDatabase
import com.example.gestion_agil.data.repository.NotificacionRepository
import com.example.gestion_agil.databinding.FragmentSlideshowBinding

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SlideshowViewModel
    private lateinit var adapter: NotificacionAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)

        val dao = AppDatabase.getDatabase(requireContext()).NotificacionDao()
        val repo = NotificacionRepository(dao)
        val factory = SlideShowViewModelFactory(repo)

        viewModel = ViewModelProvider(this, factory)[SlideshowViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()

        viewModel.notificaciones.observe(viewLifecycleOwner) { lista ->
            adapter.updateData(lista)
            showEmptyView(lista.isEmpty())
        }
    }

    private fun setupRecycler() {
        adapter = NotificacionAdapter(emptyList())
        binding.recyclerViewNotifications.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewNotifications.adapter = adapter
    }

    private fun showEmptyView(isEmpty: Boolean) {
        binding.recyclerViewNotifications.visibility = if (isEmpty) View.GONE else View.VISIBLE
        binding.textNoNotifications.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}