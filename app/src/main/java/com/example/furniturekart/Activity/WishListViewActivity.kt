package com.example.furniturekart.Activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furniturekart.Adapter.ProductAdapter
import com.example.furniturekart.Adapter.WishlistAdapter
import com.example.furniturekart.Client.ApiClient
import com.example.furniturekart.Interface.ApiInterface
import com.example.furniturekart.Model.ProductDetailModel
import com.example.furniturekart.Model.WishlistModel
import com.example.furniturekart.R
import com.example.furniturekart.databinding.ActivityProductBinding
import com.example.furniturekart.databinding.ActivityWishListViewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WishListViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWishListViewBinding
    lateinit var apiinterface: ApiInterface
    lateinit var call: Call<List<WishlistModel>>
    lateinit var list:MutableList<WishlistModel>

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
        binding.wisherror.visibility = View.GONE
    }

    private fun connected()
    {
        // Toast.makeText(applicationContext,"Connected",Toast.LENGTH_LONG).show()
        val inflatLayout = findViewById<View>(R.id.networkError)
        inflatLayout.visibility = View.GONE
        binding.wisherror.visibility = View.VISIBLE
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWishListViewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setTitle("My Wishlist")

        apiinterface=  ApiClient.getapiclient().create(ApiInterface::class.java)
        list = ArrayList()

        var layoutManager: RecyclerView.LayoutManager = GridLayoutManager(this,2)
        binding.recycler.layoutManager=layoutManager




        var call: Call<List<WishlistModel>> = apiinterface.wishlistviewdata()
        call.enqueue(object: Callback<List<WishlistModel>>
        {
            override fun onResponse(call: Call<List<WishlistModel>>, response: Response<List<WishlistModel>>) {
                list = response.body() as MutableList<WishlistModel>

                var wadapter = WishlistAdapter(applicationContext,list)
                binding.recycler.adapter=wadapter
            }
            override fun onFailure(call: Call<List<WishlistModel>>, t: Throwable) {
            }
        })
    }


    override fun onStart() {
        super.onStart()
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }
    override fun onBackPressed() {

        var builder = AlertDialog.Builder(this)
        builder.setTitle("Go to Dashboard...")
        builder.setPositiveButton("YES", { dialogInterface: DialogInterface, i: Int ->
            var i = Intent(applicationContext, DashActivity::class.java)
            startActivity(i)
        })
        builder.setNegativeButton("NO",{ dialogInterface: DialogInterface, i: Int -> })
        builder.show()
    }
}