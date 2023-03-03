package com.example.newsapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.newsapp.R
import com.example.newsapp.adapters.TabLayoutAdapter
import com.example.newsapp.databinding.FragmentNewsBinding
import com.google.android.material.tabs.TabLayout


class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding
    private lateinit var tabLayout : TabLayout
    private lateinit var viewPager : ViewPager



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = binding.tabLayout
        viewPager = binding.viewPager

        tabLayout.addTab(tabLayout.newTab().setText("Live News"))
        tabLayout.addTab(tabLayout.newTab().setText("Sports News"))
        tabLayout.addTab(tabLayout.newTab().setText("Sciences News"))

        tabLayout.tabGravity = TabLayout.GRAVITY_FILL


        val tabLayoutAdapter =
            TabLayoutAdapter(requireContext(), childFragmentManager, tabLayout.tabCount)
        viewPager.adapter = tabLayoutAdapter

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

    }
}