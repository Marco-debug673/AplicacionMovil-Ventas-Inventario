package com.example.gestion_agil.ui.reflow

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ReflowViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProductosFragment()
            1 -> HistorialInventarioFragment()
            else -> throw IllegalStateException("Posición de pestaña no válida: $position")
        }
    }
}