package com.example.furniturekart.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.furniturekart.Activity.ForgotPassActivity

import com.example.furniturekart.Client.ApiClient
import com.example.furniturekart.Interface.ApiInterface
import com.example.furniturekart.Model.CategoryModel
import com.example.furniturekart.Model.UserViewModel
import com.example.furniturekart.R
import com.squareup.picasso.Picasso


class UserViewAdapter (var context: Context, var list2: List<UserViewModel>): RecyclerView.Adapter<MyView5>() {

    lateinit var apiInterface: ApiInterface

    var countryFilterList = ArrayList<UserViewModel>()

    init {
        countryFilterList.addAll(list2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView5 {
        var layoutInflater = LayoutInflater.from(parent.context)
        var view = layoutInflater.inflate(R.layout.design_userlist, parent, false)
        return MyView5(view)
    }

    override fun getItemCount(): Int {
        return countryFilterList.size
    }

    override fun onBindViewHolder(holder: MyView5, position: Int) {
        holder.fname.setText(list2.get(position).fname)
        holder.lname.setText(list2.get(position).lname)
        holder.phoneno.setText(list2.get(position).phone)


        apiInterface =  ApiClient.getapiclient().create(ApiInterface::class.java)


        holder.itemView.setOnClickListener {
            var i = Intent(context, ForgotPassActivity::class.java)
            i.putExtra("id", list2.get(position).id)
            i.putExtra("fname", list2.get(position).fname)
            i.putExtra("lname", list2.get(position).lname)
            i.putExtra("phoneno", list2.get(position).phone)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(i)
        }
        val item = countryFilterList[position]
        holder.fname.text = item.fname
        holder.phoneno.text = item.phone
    }
    fun filter(query: String)
    {
        countryFilterList.clear()
        if(query.isEmpty())
        {
            countryFilterList.addAll(list2)
        }
        else
        {
            val lower = query.toLowerCase()

            for(item in list2)
            {
                if(item.fname.toLowerCase().contains(lower))
                {
                    countryFilterList.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }

}
class MyView5(Itemview: View) : RecyclerView.ViewHolder(Itemview) {

    var fname: TextView = Itemview.findViewById(R.id.fname)
    var lname: TextView = Itemview.findViewById(R.id.lname)
    var phoneno: TextView = Itemview.findViewById(R.id.phoneno)
}