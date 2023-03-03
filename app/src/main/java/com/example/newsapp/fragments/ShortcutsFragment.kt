package com.example.newsapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentShortcutsBinding


class ShortcutsFragment : Fragment() {

    private lateinit var binding: FragmentShortcutsBinding

    val argsLive: ShortcutsFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shortcuts, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.returnBtnShortcuts.setOnClickListener {





            var x = arguments?.let { ShortcutsFragmentArgs.fromBundle(it).liveVariable }

            when (x) {
                arguments?.let { ShortcutsFragmentArgs.fromBundle(it).liveVariable } -> {
                    findNavController().navigate(R.id.action_shortcutsFragment_to_detailLiveFragment)
                }

                arguments?.let { ShortcutsFragmentArgs.fromBundle(it).sportsVariable }-> {
                    findNavController().navigate(R.id.action_shortcutsFragment_to_detailSportsFragment)

                }

                arguments?.let { ShortcutsFragmentArgs.fromBundle(it).sciencesVariable }-> {
                    findNavController().navigate(R.id.action_shortcutsFragment_to_detailSciencesFragment)

                }

                arguments?.let { ShortcutsFragmentArgs.fromBundle(it).otherTopicsVariable }-> {
                    findNavController().navigate(R.id.action_shortcutsFragment_to_otherTopicFragment)

                }
                else -> {}
            }

        }
    }

}