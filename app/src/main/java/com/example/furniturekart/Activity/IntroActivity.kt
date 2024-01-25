package com.example.furniturekart.Activity

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.viewpager.widget.ViewPager
import com.example.furniturekart.Adapter.IntroAdapter
import com.example.furniturekart.R
import com.google.android.gms.common.api.GoogleApiClient

class IntroActivity : AppCompatActivity() {


   lateinit var viewPager: ViewPager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

//  VIEWPAGER
        viewPager = findViewById(R.id.viewPager)
        val Adapter = IntroAdapter(supportFragmentManager)
        viewPager.run { this.setAdapter(Adapter) }

// SHAREDPREFERENCS = CHECK IF APPLICATION IS OPEN FOR THE FIRST TIME
                    val shared = getSharedPreferences("INTRO", MODE_PRIVATE)
                    val FirstTime = shared.getString("FirstTime", "")

                    if (FirstTime.equals("yes")) {
                        val i = Intent(applicationContext, DashActivity::class.java)
                        startActivity(i)
                    } else {
                        val editor:SharedPreferences.Editor = shared.edit()
                        editor.putString("FirstTime", "yes")
                        editor.apply()
                    }
        }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {

        var aleart = AlertDialog.Builder(this)
        aleart.setTitle("Are you Exit")

        aleart.setPositiveButton("YES", { dialogInterface: DialogInterface, i: Int ->
            finishAffinity()
        })
        aleart.setNegativeButton("NO", { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.cancel()
        })
        aleart.show()
    }
    }