package com.mercury.alcyone.Presentation.TabLayout

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val NUM_PAGES = 2

class AdapterTabLayout(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FirstSubGroupFragment()
            1 -> SecondSubGroupFragment()
            else-> FirstSubGroupFragment()
        }
    }
}