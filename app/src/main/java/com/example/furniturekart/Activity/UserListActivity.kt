package com.example.furniturekart.Activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furniturekart.Adapter.CartAdapter
import com.example.furniturekart.Adapter.DashboardAdapter
import com.example.furniturekart.Adapter.UserViewAdapter
import com.example.furniturekart.Client.ApiClient
import com.example.furniturekart.Interface.ApiInterface
import com.example.furniturekart.Model.UserViewModel
import com.example.furniturekart.R
import com.example.furniturekart.databinding.ActivityUserListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserListBinding
    lateinit var apiinterface: ApiInterface
    lateinit var call: Call<List<UserViewModel>>
    lateinit var list2: MutableList<UserViewModel>
    lateinit var searchView: androidx.appcompat.widget.SearchView



    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val notConnected =
                intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
            if (notConnected) {
                disconnected()
            } else {
                connected()
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun disconnected() {
        Toast.makeText(applicationContext, "not Connected", Toast.LENGTH_LONG).show()

        val inflatLayout = findViewById<View>(R.id.networkError)
        inflatLayout.visibility = View.VISIBLE
        binding.dashError.visibility = View.GONE
    }

    private fun connected() {
        // Toast.makeText(applicationContext,"Connected",Toast.LENGTH_LONG).show()
        val inflatLayout = findViewById<View>(R.id.networkError)
        inflatLayout.visibility = View.GONE
        binding.dashError.visibility = View.VISIBLE

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        apiinterface = ApiClient.getapiclient().create(ApiInterface::class.java)
        list2 = ArrayList()

        var layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.recycler.layoutManager = layoutManager


        var call: Call<List<UserViewModel>> = apiinterface.userviewdata()
        call.enqueue(object : Callback<List<UserViewModel>> {
            override fun onResponse(call: Call<List<UserViewModel>>, response: Response<List<UserViewModel>>
            ) {
                list2 = response.body() as MutableList<UserViewModel>

                var uadapter = UserViewAdapter(applicationContext,list2)
                binding.recycler.adapter = uadapter
            }

            override fun onFailure(call: Call<List<UserViewModel>>, t: Throwable) {
            }
        })

        searchView = findViewById(R.id.searchView)
        searchView.queryHint = "Search your account..."
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                (binding.recycler.adapter as UserViewAdapter?)?.filter(newText!!)
                return false
            }
        })
    }
    override fun onStart() {
        super.onStart()
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }
}