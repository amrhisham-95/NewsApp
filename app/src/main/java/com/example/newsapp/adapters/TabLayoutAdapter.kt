@file:Suppress("DEPRECATION")

package com.example.newsapp.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.newsapp.fragments.GeneralNewsFragment
import com.example.newsapp.fragments.SciencesFragment
import com.example.newsapp.fragments.SportsFragment

internal class TabLayoutAdapter(var context : Context, fm:FragmentManager, private var totalTabs: Int) : FragmentPagerAdapter(fm){
    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> {GeneralNewsFragment()}
            1 -> {SportsFragment()}
            else -> {SciencesFragment()}
        }
    }

}