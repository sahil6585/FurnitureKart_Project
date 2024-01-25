package com.example.furniturekart.Activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Paint
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.furniturekart.Adapter.DashboardAdapter
import com.example.furniturekart.Client.ApiClient
import com.example.furniturekart.Interface.ApiInterface
import com.example.furniturekart.Model.CategoryModel
import com.example.furniturekart.R
import com.example.furniturekart.databinding.ActivityFullScreenProductBinding
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FullScreenProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFullScreenProductBinding
    lateinit var apiInterface: ApiInterface


    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val notConnected = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
            if (notConnected)
            {
                disconnected()
            }
            else
            {
                connected()
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun disconnected()
    {
        Toast.makeText(applicationContext,"not Connected", Toast.LENGTH_LONG).show()

        val inflatLayout = findViewById<View>(R.id.networkError)
        inflatLayout.visibility = View.VISIBLE
        binding.fullscreenerror.visibility = View.GONE
    }

    private fun connected()
    {
        // Toast.makeText(applicationContext,"Connected",Toast.LENGTH_LONG).show()
        val inflatLayout = findViewById<View>(R.id.networkError)
        inflatLayout.visibility = View.GONE
        binding.fullscreenerror.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullScreenProductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.fprice.setPaintFlags(binding.fprice.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)

        apiInterface =  ApiClient.getapiclient().create(ApiInterface::class.java)
        setSupportActionBar(binding.toolbar)

        var i = intent
        var id = i.getIntExtra("id",101)
        var c_id = i.getIntExtra("c_id",102)
        var mrp= i.getIntExtra("mrp",0)
        var fake_mrp= i.getIntExtra("fake_mrp",0)
        var p_per= i.getIntExtra("p_per",0)
        var p_brand=i.getStringExtra("brand")
        var p_name=  i.getStringExtra("name")
        var p_image= i.getStringExtra("image")


        binding.brand.setText(p_brand)
        binding.info.setText( i.getStringExtra("info"))
        Picasso.get().load(p_image).into(binding.image)
        binding.name.setText(p_name)
        binding.mrp.setText(mrp.toString())
        binding.fprice.setText(fake_mrp.toString())
        binding.dicount.setText(p_per.toString())
        binding.color.setText( i.getStringExtra("color"))
        binding.material.setText( i.getStringExtra("material"))
        binding.weight.setText( i.getStringExtra("weight"))
        binding.itemnumber.setText( i.getStringExtra("no_of_item"))
        binding.itempiece.setText( i.getStringExtra("no_of_pieces"))
        binding.menufacturer.setText( i.getStringExtra("menufacturer"))
        binding.country.setText( i.getStringExtra("country"))

        //binding.photoView.setImageResource(i.getStringExtra("image"))

        binding.wishlist.setOnClickListener{

            var call: Call<Void> = apiInterface.wishlistdata(c_id, p_brand.toString(), p_name.toString() ,mrp , p_image.toString())

            call.enqueue(object: Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>)
                {
                     Toast.makeText(applicationContext,"ADD TO WISHLIST",Toast.LENGTH_LONG).show()
                    startActivity(Intent(applicationContext,WishListViewActivity::class.java))
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(applicationContext,"No Internet",Toast.LENGTH_LONG).show()
                }
            })
        }
        binding.cart.setOnClickListener {
            var call: Call<Void> = apiInterface.cartdata(c_id, p_brand.toString(), p_name.toString() ,mrp , p_image.toString())

            call.enqueue(object: Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>)
                {
                    Toast.makeText(applicationContext,"ADD TO CART",Toast.LENGTH_LONG).show()
                    startActivity(Intent(applicationContext,CartviewActivity::class.java))
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(applicationContext,"No Internet",Toast.LENGTH_LONG).show()
                }
            })

        }

    }
    override fun onStart() {
        super.onStart()
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cart_option,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return super.onOptionsItemSelected(item)
    }
}