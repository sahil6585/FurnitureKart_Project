package com.example.furniturekart.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.example.furniturekart.R

class IntroFragment1 : Fragment() {

    lateinit var next1: TextView
    lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_intro1, container, false)
        viewPager = requireActivity().findViewById(R.id.viewPager)
        next1 = view.findViewById(R.id.nextone)
        next1.setOnClickListener {
            viewPager.run { this.setCurrentItem(1) }
        }

        return view
    }

}