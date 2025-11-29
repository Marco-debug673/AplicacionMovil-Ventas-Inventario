package com.example.gestion_agil.ui.reflow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gestion_agil.R
import com.example.gestion_agil.databinding.FragmentReflowBinding
import com.google.android.material.tabs.TabLayoutMediator

class ReflowFragment : Fragment() {

    private var _binding: FragmentReflowBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReflowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Se crea y asigna un nuevo adaptador cada vez que la vista se crea.
        // Se usa el constructor que solo pide el Fragmento ('this').
        val viewPagerAdapter = ReflowViewPagerAdapter(this)
        binding.viewPager.adapter = viewPagerAdapter

        // Se conecta el TabLayout con el ViewPager2.
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.opcion3)
                1 -> getString(R.string.opcion4)
                else -> null
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}