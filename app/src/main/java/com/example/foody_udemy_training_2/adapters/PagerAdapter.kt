package com.example.foody_udemy_training_2.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapter(
    private val resultBundle: Bundle,
    private val  fragments: ArrayList<Fragment>,
    private val  title: ArrayList<String>,
    private val  fm: FragmentManager
// BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT: indicates that only the current fragment will be in the resume state.
// all other fragments are capped at started state
): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,) {

    // return fragments, not size, and it will return the number of three,
    // because there are three fragments which we are going to add
    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        // we're going to pass all the result from our recipe to those fragments
        fragments[position].arguments = resultBundle
        return fragments[position]
    }

    // return the title of those fragments
    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }
}