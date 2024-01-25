package com.example.furniturekart.Activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.furniturekart.Client.ApiClient
import com.example.furniturekart.Interface.ApiInterface
import com.example.furniturekart.R
import com.example.furniturekart.databinding.ActivityPaymentBinding
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class paymentActivity : AppCompatActivity(), PaymentResultListener, RadioGroup.OnCheckedChangeListener {

    private lateinit var binding: ActivityPaymentBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var apiinterface: ApiInterface

    var price=0
    var paymentmethod =" "


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


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        apiinterface = ApiClient.getapiclient().create(ApiInterface::class.java)

        Toast.makeText(applicationContext, ""+paymentmethod, Toast.LENGTH_SHORT).show()


        val i = intent
        val totalprice = i.getIntExtra("totalprice", 0)
        val value = i.getIntExtra("value", 0)
        val id = i.getIntExtra("id", 0)
        val name = i.getStringExtra("name")
        val brand = i.getStringExtra("brand")
        price = i.getIntExtra("price",101)
        val image = i.getStringExtra("image")
        sharedPreferences = getSharedPreferences("USER", Context.MODE_PRIVATE)


        binding.Name.text = name
        binding.totalprice.text = totalprice.toString()
        binding.value.text = value.toString()
        binding.brand.text = brand
        binding.Price.text = price.toString()
        Picasso.get().load(image).into(binding.imageView)

       binding.radioGroup.setOnCheckedChangeListener(this)
        binding.ordBtn.setOnClickListener {

            var pbrand = binding.brand.text.toString()
            var pname = binding.Name.text.toString()
            var pquantity = binding.value.text.toString()
            var pprice = binding.Price.text.toString()
            var totalprice = binding.totalprice.text.toString()
            var uname = binding.fname.text.toString()
            var usurname = binding.lname.text.toString()
            var uaddress = binding.address.text.toString()
            var contact = binding.contact.text.toString()



            if (uname.length == 0 && usurname.length == 0 && uaddress.length == 0 && contact.length == 0 )
            {
                binding.fname.setError("Please Enter username")
                binding.lname.setError("Please Enter surname")
                binding.address.setError("Please Enter address")
                binding.contact.setError("Please Enter contact")
            }
            else if (uname.length == 0) {
                binding.fname.setError("Please enter Firstname")
            }
            else if (usurname.length == 0) {
                binding.lname.setError("Please enter Surname")
            }
            else if (uaddress.length == 0) {
                binding.address.setError("Please enter Address")
            }
            else if (contact.length == 0) {
                binding.address.setError("Please enter Contact number")
            }
            else if (contact.length <= 9 && contact.length >= 11) {
                binding.contact.setError("Valid Only 10 digits")
            }

            else {
                var call: Call<Void> = apiinterface.palceorder(
                    pbrand,
                    pname,
                    pquantity,
                    pprice,
                    totalprice,
                    uname,
                    usurname,
                    contact,
                    uaddress,
                    paymentmethod
                )

                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        var i = Intent(applicationContext,PayInfoActivity::class.java)
                        startActivity(i)
                        Toast.makeText(applicationContext, "CONFIRM ORDER", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(applicationContext, "ORDER FAIL", Toast.LENGTH_SHORT).show()

                    }
                })
            }

        }


        //FOR ONLINE PAYMENT
        binding.makePaymentBtn.setOnClickListener{

            var finaldata= price

            Log.d("topsprice",finaldata.toString())
            val pPrice = Integer.parseInt(totalprice.toString())*100
            val checkout = Checkout()
            checkout.setKeyID("rzp_test_pJ8ElvmChXIyZC")
            /*rzp_test_KOyE4EKQ2KOwOS*/
            val obj = JSONObject()
            try {
                obj.put("name", name)
               obj.put("brand", brand)
                obj.put("theme.color", "")
                obj.put("currency", "INR")
                obj.put("amount", pPrice)
                obj.put("prefill.contact", sharedPreferences.getString("mob", ""))
                obj.put("prefill.email", "jchirag2000@gmail.com")
                checkout.open(this, obj)

            } catch (e: JSONException)
            {
                e.printStackTrace()
            }

        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Payment Success $p0", Toast.LENGTH_SHORT).show()

        var pbrand = binding.brand.text.toString()
        var pname = binding.Name.text.toString()
        var pquantity = binding.value.text.toString()
        var pprice = binding.Price.text.toString()
        var totalprice = binding.totalprice.text.toString()
        var uname = binding.fname.text.toString()
        var usurname = binding.lname.text.toString()
        var uaddress = binding.address.text.toString()
        var contact = binding.contact.text.toString()


        if (uname.length == 0 && usurname.length == 0 && uaddress.length == 0 && contact.length == 0 )
        {
            binding.fname.setError("Please Enter username")
            binding.lname.setError("Please Enter surname")
            binding.address.setError("Please Enter address")
            binding.contact.setError("Please Enter contact")
        }
        else if (uname.length == 0) {
            binding.fname.setError("Please enter Firstname")
        }
        else if (usurname.length == 0) {
            binding.lname.setError("Please enter Surname")
        }
        else if (uaddress.length == 0) {
            binding.address.setError("Please enter Address")
        }
        else if (contact.length == 0) {
            binding.address.setError("Please enter Contact number")
        }
        else if (contact.length <= 9 && contact.length >= 11) {
            binding.contact.setError("Valid Only 10 digits")
        }

       else
       {

        var call: Call<Void> = apiinterface.palceorder(pbrand,pname,pquantity,pprice,totalprice,uname,usurname,contact,uaddress,paymentmethod)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                var i = Intent(applicationContext,PayInfoActivity::class.java)
                startActivity(i)
                Toast.makeText(applicationContext, "ORDER CONFIRM", Toast.LENGTH_SHORT).show()

            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(applicationContext, "ORDER FAIL", Toast.LENGTH_SHORT).show()

            }
        })
        }
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Payment Failed ", Toast.LENGTH_SHORT).show()
    }

    override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
        if(binding.netbanking.isChecked)
        {
            paymentmethod="Net Banking"
            binding.ordBtn.visibility = View.GONE
            binding.makePaymentBtn.visibility = View.VISIBLE
        }
        if(binding.cod.isChecked)
        {
            paymentmethod="Cash On Delivery"
            binding.ordBtn.visibility = View.VISIBLE
            binding.makePaymentBtn.visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }
}

