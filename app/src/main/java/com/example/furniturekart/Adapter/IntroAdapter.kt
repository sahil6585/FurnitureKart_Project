package com.example.furniturekart.Adapter

import android.support.v4.os.IResultReceiver2.Default
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.furniturekart.Fragment.IntroFragment1
import com.example.furniturekart.Fragment.IntroFragment2
import com.example.furniturekart.Fragment.IntroFragment3
import com.example.furniturekart.Fragment.IntroFragment4

class IntroAdapter(fm:FragmentManager) : FragmentPagerAdapter(fm)
{
    override fun getCount(): Int {
        return 4
    }

    override fun getItem(position: Int): Fragment {
        when(position)
        {
            0->
            {
                return IntroFragment1()
            }
            1->
            {
                return IntroFragment2()
            }
            2->
            {
                return IntroFragment3()
            }
            3->
            {
                return IntroFragment4()
            }
         else->
         {
             return error("error")
         }
        }
    }
}