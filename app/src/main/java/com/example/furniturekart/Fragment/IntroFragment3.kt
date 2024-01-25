package com.example.furniturekart.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.example.furniturekart.R

class IntroFragment3 : Fragment() {

    lateinit var next3: TextView
    lateinit var back: TextView
    lateinit var viewPager: ViewPager


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_intro3, container, false)
        viewPager = requireActivity().findViewById(R.id.viewPager)
        next3 = view.findViewById(R.id.nextthree)
        back = view.findViewById(R.id.backthree)
        next3.setOnClickListener {
            viewPager.run { this.setCurrentItem(3) }

        }
        back.setOnClickListener {
            viewPager.run { this.setCurrentItem(1) }
        }
        return view
    }

}