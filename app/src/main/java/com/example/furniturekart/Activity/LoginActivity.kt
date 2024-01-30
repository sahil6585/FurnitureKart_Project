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
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.furniturekart.Client.ApiClient
import com.example.furniturekart.Interface.ApiInterface
import com.example.furniturekart.Model.RegModel
import com.example.furniturekart.R
import com.example.furniturekart.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    var gso: GoogleSignInOptions? = null
    var gsc: GoogleSignInClient? = null


    private lateinit var binding: ActivityLoginBinding
    lateinit var apiinterface: ApiInterface
    lateinit var loginActivity:LoginActivity
    lateinit var shared: SharedPreferences

    //FOR CHECK INTERNET CONNECTION
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
        Toast.makeText(applicationContext,"not Connected",Toast.LENGTH_LONG).show()

        val inflatLayout = findViewById<View>(R.id.networkError)
        inflatLayout.visibility = View.VISIBLE

        binding.error.visibility = View.GONE
    }

    private fun connected()
    {
        //Toast.makeText(applicationContext,"Connected",Toast.LENGTH_LONG).show()

        val inflatLayout = findViewById<View>(R.id.networkError)
        inflatLayout.visibility = View.GONE

        binding.error.visibility = View.VISIBLE
    }

    //MAIN METHOD
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        apiinterface = ApiClient.getapiclient().create(ApiInterface::class.java)

        binding.goReg1.setOnClickListener {
            var i = Intent(applicationContext,RegistrationActivity::class.java)
            startActivity(i)
        }
        binding.goReg2.setOnClickListener {
            var i = Intent(applicationContext,RegistrationActivity::class.java)
            startActivity(i)
        }

        binding.forgot.setOnClickListener {
            var i = Intent(applicationContext,UserListActivity::class.java)
            startActivity(i)
        }
        //SHAREDPREFERENCES
        shared=getSharedPreferences("USER", Context.MODE_PRIVATE)
        if (shared.getBoolean("USER",false) && !shared.getString("phone","")!!.isEmpty())
        {
            startActivity(Intent(applicationContext,DashActivity::class.java))
            finish()
        }

        binding.loginBtn.setOnClickListener {

            var phone = binding.loginphone.text.toString()
            var password = binding.loginpass.text.toString()

            if (phone.length == 0 && password.length == 0) {
                binding.loginphone.setError("Please Enter username")
                binding.loginpass.setError("Please Enter password")
            } else if (phone.length == 0) {
                binding.loginphone.setError("Please Enter username")
            } else if (password.length == 0) {
                binding.loginpass.setError("Please Enter password")
            } else if (phone.length <= 9) {
                binding.loginphone.setError("Please enter correct number ")
            }
            else if (phone.length >13 ) {
                binding.loginphone.setError("Number required")
            }else if (password.length <= 3) {
                binding.loginpass.setError("Password minimum 4 digit")
            } else {

                var call: Call<RegModel> = apiinterface.logindata(phone,password)

                call.enqueue(object : Callback<RegModel> {
                    override fun onResponse(call: Call<RegModel>, response: Response<RegModel>) {
                        Toast.makeText(applicationContext, "LOGIN SUCESSFUL", Toast.LENGTH_SHORT)
                            .show()
                        //SHAREDPREFERENCES
                        var i = Intent(applicationContext, IntroActivity::class.java)
                        var editor: SharedPreferences.Editor = shared.edit()
                        editor.putBoolean("USER", true)
                        editor.putString("phone", phone)
                        editor.putString("password", password)
                        editor.apply()
                        startActivity(i)
                    }

                    override fun onFailure(call: Call<RegModel>, t: Throwable) {
                        Toast.makeText(applicationContext, "LOGIN FAIL", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

//GOOGLE LOGIN
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(this, gso!!)
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            navigateToSecondActivity()
        }

        binding.google.setOnClickListener {
            signIn()
        }

    }
    //GOOGLE LOGIN
    fun signIn() {
        val signInIntent = gsc!!.signInIntent
        startActivityForResult(signInIntent, 1000)
    }
    //GOOGLE LOGIN
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                task.getResult(ApiException::class.java)
                navigateToSecondActivity()
            } catch (e: ApiException) {
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
    //GOOGLE LOGIN
    fun navigateToSecondActivity() {
        finish()
        val intent = Intent(applicationContext, DashActivity::class.java)
        startActivity(intent)
    }

    //internet connection
    override fun onStart() {
        super.onStart()
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
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