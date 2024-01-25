package com.example.furniturekart.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.example.furniturekart.R


class IntroFragment2 : Fragment() {

    lateinit var next2: TextView
    lateinit var back: TextView
    lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_intro2, container, false)
        viewPager = requireActivity().findViewById(R.id.viewPager)
        next2 = view.findViewById(R.id.nexttwo)
        back = view.findViewById(R.id.backtwo)
        next2.setOnClickListener {
            viewPager.run { this.setCurrentItem(2) }
        }
        back.setOnClickListener {
            viewPager.run { this.setCurrentItem(0) }
        }
        return view
    }
}