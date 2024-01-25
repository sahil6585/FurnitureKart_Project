package com.example.furniturekart.Activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Paint
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.furniturekart.Adapter.DashboardAdapter
import com.example.furniturekart.Adapter.ProductAdapter
import com.example.furniturekart.Client.ApiClient
import com.example.furniturekart.Interface.ApiInterface
import com.example.furniturekart.Model.ProductDetailModel
import com.example.furniturekart.R
import com.example.furniturekart.databinding.ActivityProductBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityProductBinding
    lateinit var apiinterface: ApiInterface
    lateinit var call: Call<List<ProductDetailModel>>
    lateinit var list:MutableList<ProductDetailModel>
    lateinit var searchView: androidx.appcompat.widget.SearchView

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
        binding.producterror.visibility = View.GONE
    }

    private fun connected()
    {
        // Toast.makeText(applicationContext,"Connected",Toast.LENGTH_LONG).show()
        val inflatLayout = findViewById<View>(R.id.networkError)
        inflatLayout.visibility = View.GONE
        binding.producterror.visibility = View.VISIBLE
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.swipe.setOnRefreshListener(this)

        setSupportActionBar(binding.toolbar)
        //supportActionBar!!.setTitle("")

        apiinterface=  ApiClient.getapiclient().create(ApiInterface::class.java)
        list = ArrayList()

        var layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.recycler.layoutManager=layoutManager

        var i = intent
        var id = i.getIntExtra("mypos",101)

        var call:Call<List<ProductDetailModel>> = apiinterface.ProductDetailsviewdata(id)
        call.enqueue(object: Callback<List<ProductDetailModel>>
        {
            override fun onResponse(call: Call<List<ProductDetailModel>>, response: Response<List<ProductDetailModel>>) {
                list = response.body() as MutableList<ProductDetailModel>

                var cadapter = ProductAdapter(applicationContext,list)
                binding.recycler.adapter=cadapter




               // cadapter.notifyDataSetChanged()

             //   binding.recycler.adapter!!.notifyDataSetChanged()
            }
            override fun onFailure(call: Call<List<ProductDetailModel>>, t: Throwable) {
            }
        })

        /*searchView = findViewById(R.id.searchView)
        searchView.queryHint = "Search product brand..."
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                (binding.recycler.adapter as ProductAdapter?)?.filter(newText!!)
                return false
            }
        })*/
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

    override fun onRefresh() {
        var i2 = intent
        var id2 = i2.getIntExtra("mypos",102)

        var call:Call<List<ProductDetailModel>> = apiinterface.ProductDetailsviewdata(id2)
        call.enqueue(object: Callback<List<ProductDetailModel>> {
            override fun onResponse(
                call: Call<List<ProductDetailModel>>,
                response: Response<List<ProductDetailModel>>
            ) {
                list = response.body() as MutableList<ProductDetailModel>

                var cadapter = ProductAdapter(applicationContext, list)
                binding.recycler.adapter = cadapter
                binding.swipe.isRefreshing=false
            }

            override fun onFailure(call: Call<List<ProductDetailModel>>, t: Throwable) {
                binding.swipe.isRefreshing=true
            }
        })
    }

}