package com.example.furniturekart.Fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.viewpager.widget.ViewPager
import com.example.furniturekart.Activity.DashActivity
import com.example.furniturekart.R


class IntroFragment4 : Fragment() {

    lateinit var start: Button
    lateinit var viewPager: ViewPager

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_intro4, container, false)
        viewPager = requireActivity().findViewById(R.id.viewPager)
        start = view.findViewById(R.id.start_btn)

        start.setOnClickListener {
            startActivity(Intent(requireActivity(), DashActivity::class.java))
        }
        return view
    }
}