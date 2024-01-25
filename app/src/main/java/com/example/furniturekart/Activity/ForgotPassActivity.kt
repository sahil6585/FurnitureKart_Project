package com.example.furniturekart.Activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import com.example.furniturekart.Client.ApiClient
import com.example.furniturekart.Interface.ApiInterface
import com.example.furniturekart.R
import com.example.furniturekart.databinding.ActivityForgotPassBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class ForgotPassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPassBinding
    lateinit var apiinterface: ApiInterface
    var dialog: ProgressDialog? = null
    lateinit var varificationid:String
    lateinit var auth: FirebaseAuth
    lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var Id = 0

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
        binding = ActivityForgotPassBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()
        apiinterface = ApiClient.getapiclient().create(ApiInterface::class.java)

        var i = intent

        var id = i.getIntExtra("id", 101)
        var fname = i.getStringExtra("fname")
        var lname = i.getStringExtra("lname")
        var phoneno = i.getStringExtra("phoneno")

        Id = id

        binding.namee.setText(fname)
        binding.lnamee.setText(lname)
        binding.phoneno.setText(phoneno)

        binding.send.setOnClickListener {
            dialog = ProgressDialog(this)
            dialog!!.setMessage("Sending OTP...")
            dialog!!.setCancelable(false)
            dialog!!.show()

            var num = binding.phoneno.text.toString()

            if (num.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "please enter a valid number",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                sendverificationcode(num)
            }
        }
        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                var code = p0.smsCode
                if (code != null) {
                    binding.otp.setText(code)
                } else {
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show();
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_LONG).show()
                dialog!!.dismiss()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                varificationid = p0
                dialog!!.dismiss()
            }
        }
        binding.changeBtn.setOnClickListener {
            var otp = binding.otp.text.toString()
            var password = binding.pass.text.toString()


            if (password.length <= 3) {
                binding.pass.setError("Password minimum 4 digit")
            } else {
                if (otp.isEmpty()) {
                    Toast.makeText(applicationContext, "please enter a valid OTP", Toast.LENGTH_SHORT).show()
                } else {
                    verifycode(otp)
                }
            }
        }
    }

        private fun verifycode(otp: String) {
            var credential = PhoneAuthProvider.getCredential(varificationid,otp)
            signinwithcredential(credential)
        }

        private fun signinwithcredential(credential: PhoneAuthCredential) {
            auth.signInWithCredential(credential).addOnCompleteListener {
                if(it.isSuccessful)
                {
                    Toast.makeText(applicationContext, "PASSWORD CHANGED", Toast.LENGTH_SHORT).show()

                    var password = binding.pass.text.toString()
                    var call: Call<Void> = apiinterface.forgotpass(Id,password)

                    call.enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            Toast.makeText(applicationContext,"Succesful", Toast.LENGTH_LONG).show()
                            startActivity(Intent(applicationContext,LoginActivity::class.java))
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(applicationContext,"Fail",Toast.LENGTH_LONG).show()
                        }
                    })
                }
                else
                {
                    Toast.makeText(applicationContext, "Invalid OTP", Toast.LENGTH_SHORT).show()

                }
            }
                .addOnFailureListener {
                    Toast.makeText(applicationContext, "FAIL", Toast.LENGTH_SHORT).show()

                }
        }

        private fun sendverificationcode(num: String) {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(num,60, TimeUnit.SECONDS,this,mCallback)
        }
    override fun onStart() {
        super.onStart()
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }
}