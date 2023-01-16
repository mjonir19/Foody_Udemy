package com.example.foody_udemy_training_2.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(
    private val resultBundle: Bundle,
    private val  fragments: ArrayList<Fragment>,
    fragmentActivity: FragmentActivity
// BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT: indicates that only the current fragment will be in the resume state.
// all other fragments are capped at started state
): FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        // we're going to pass all the result from our recipe to those fragments
        fragments[position].arguments = resultBundle
        return fragments[position]
    }
}