package com.example.gestion_agil.ui.transform

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2 // Tenemos dos pestañas
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TabVentaFragment()
            1 -> TabHistorialFragment()
            else -> throw IllegalStateException("Posición no válida: $position")
        }
    }
}
