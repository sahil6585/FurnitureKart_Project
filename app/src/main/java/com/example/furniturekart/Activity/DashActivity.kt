package com.example.furniturekart.Activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.TextSliderView

import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.furniturekart.Adapter.DashboardAdapter
import com.example.furniturekart.Client.ApiClient
import com.example.furniturekart.Interface.ApiInterface
import com.example.furniturekart.Model.CategoryModel
import com.example.furniturekart.R
import com.example.furniturekart.databinding.ActivityDashBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener{

    private lateinit var binding: ActivityDashBinding
    lateinit var shared: SharedPreferences
    lateinit var list: MutableList<CategoryModel>
    lateinit var apiInterface: ApiInterface
   lateinit var searchView: androidx.appcompat.widget.SearchView

    lateinit var gso: GoogleSignInOptions
    var gsc: GoogleSignInClient? = null
    var personemail =""
    var Phonee =""

    lateinit var slider: SliderLayout
    lateinit var map: HashMap<String, Int>

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
        binding.recycler.visibility = View.GONE
    }

    private fun connected() {
        // Toast.makeText(applicationContext,"Connected",Toast.LENGTH_LONG).show()
        val inflatLayout = findViewById<View>(R.id.networkError)
        inflatLayout.visibility = View.GONE
        binding.dashError.visibility = View.VISIBLE
        binding.recycler.visibility = View.VISIBLE

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        list = ArrayList()

        val imageSlider = findViewById<ImageSlider>(R.id.imageSlider)
        val imageList = ArrayList<SlideModel>()
        //image fetch

        //imageList.add(SlideModel(list.get(po).images))
        imageList.add(SlideModel(R.drawable.slide1))
        imageList.add(SlideModel(R.drawable.slide2))
        imageList.add(SlideModel(R.drawable.slide3))
        imageList.add(SlideModel(R.drawable.slide4))

        imageSlider.setImageList(imageList, ScaleTypes.FIT)

        setSupportActionBar(binding.toolbar)
        //refreshApp()

//SHAREDPREFERENCES
        shared = getSharedPreferences("USER", Context.MODE_PRIVATE)
        // Toast.makeText(applicationContext, "Welcome : "+shared.getString("phone",null), Toast.LENGTH_SHORT).show()
        Toast.makeText(applicationContext, "Welcome", Toast.LENGTH_SHORT).show()


        var layoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, 2)
        binding.recycler.layoutManager = layoutManager

        apiInterface = ApiClient.getapiclient().create(ApiInterface::class.java)

        var call: Call<List<CategoryModel>> = apiInterface.categoryviewdata()

        call.enqueue(object : Callback<List<CategoryModel>> {
            override fun onResponse(
                call: Call<List<CategoryModel>>,
                response: Response<List<CategoryModel>>
            ) {
                // Toast.makeText(applicationContext,"response",Toast.LENGTH_LONG).show()
                list = response.body() as MutableList<CategoryModel>

                var myAdapter = DashboardAdapter(applicationContext, list)
                binding.recycler.adapter = myAdapter



            }

            override fun onFailure(call: Call<List<CategoryModel>>, t: Throwable) {
                Toast.makeText(applicationContext, "No Internet", Toast.LENGTH_LONG).show()
            }
        })
        binding.swipe.setOnRefreshListener(this)

        //SEARCHVIEW
      /*  searchView = findViewById(R.id.searchView)
        searchView.queryHint = "Search catagory..."
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
         }

            override fun onQueryTextChange(newText: String?): Boolean {

                (binding.recycler.adapter as DashboardAdapter?)?.filter(newText!!)
                return false
            }
        })*/

        // FOR GOOGLE LOGOUT
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(this, gso!!)
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            var personName = acct.displayName
            var personEmail = acct.email
            personemail = personEmail!!
        }



    }


    //-------------------------------------------------------------------------------------------------------
    override fun onStart() {
        super.onStart()
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    //OPTION MENU
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dashboard_option, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logoutbtn -> {

                //GOOGLE LOGOUT
                if (personemail != null)
                {
                    gsc!!.signOut().addOnCompleteListener {
                        finish()
                        startActivity(Intent(applicationContext, LoginActivity::class.java))
                    }
                }

                //PHONE
                if(Phonee != null)
                {
                    shared.edit().clear().apply()
                    var i = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(i)
                }
            }

            R.id.wish -> {
                startActivity(Intent(applicationContext, WishListViewActivity::class.java))
            }

            R.id.cart -> {
                startActivity(Intent(applicationContext, CartviewActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {


        var builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure you want to exit?")
        builder.setPositiveButton("YES",
            { dialogInterface: DialogInterface, i: Int -> finishAffinity() })
        builder.setNegativeButton("NO", { dialogInterface: DialogInterface, i: Int -> })
        builder.show()

    }

    override fun onRefresh() {
        var call: Call<List<CategoryModel>> = apiInterface.categoryviewdata()

        call.enqueue(object : Callback<List<CategoryModel>> {
            override fun onResponse(
                call: Call<List<CategoryModel>>,
                response: Response<List<CategoryModel>>
            ) {
                // Toast.makeText(applicationContext,"response",Toast.LENGTH_LONG).show()
                list = response.body() as MutableList<CategoryModel>
                var myAdapter = DashboardAdapter(applicationContext, list)
                binding.recycler.adapter = myAdapter

                binding.swipe.isRefreshing=false
            }

            override fun onFailure(call: Call<List<CategoryModel>>, t: Throwable) {
                binding.swipe.isRefreshing=true
            }


        })
    }


}