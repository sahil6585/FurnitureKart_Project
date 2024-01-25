package com.example.furniturekart.Adapter

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.furniturekart.Activity.ProductActivity
import com.example.furniturekart.Client.ApiClient
import com.example.furniturekart.Interface.ApiInterface
import com.example.furniturekart.Model.CategoryModel
import com.example.furniturekart.R
import com.squareup.picasso.Picasso



class DashboardAdapter(var context : Context,var list:List<CategoryModel>): RecyclerView.Adapter<MyView>() {

    lateinit var apiInterface: ApiInterface
    /*var countryFilterList = ArrayList<CategoryModel>()

    init {
        countryFilterList.addAll(list)
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        var layoutInflater = LayoutInflater.from(parent.context)
        var view = layoutInflater.inflate(R.layout.dashboard_design, parent, false)
        return MyView(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {

        //apiInterface = ApiClient.getapiclient().create(ApiInterface::class.java)
        holder.categoryname.setText(list.get(position).product_name)
        Picasso.get().load(list.get(position).product_image).into(holder.categoryimg)


        holder.itemView.setOnClickListener {

            var i = Intent(context, ProductActivity::class.java)
            i.putExtra("mypos", list.get(position).id)
            i.addFlags(FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(i)
        }

        /* var item = countryFilterList[position]
        holder.categoryname.text = item.product_name
        Picasso.get().load(item.product_image).into(holder.categoryimg)


    }
    fun filter(query: String)
    {
        countryFilterList.clear()
        if(query.isEmpty())
        {
            countryFilterList.addAll(list)
        }
        else
        {
            val lower = query.toLowerCase()

            for(item in list)
            {
                if(item.product_name.toLowerCase().contains(lower))
                {
                    countryFilterList.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }*/
    }
}
class MyView(Itemview:View):RecyclerView.ViewHolder(Itemview)
{
    var categoryname : TextView = Itemview.findViewById(R.id.category)
    var categoryimg :ImageView = Itemview.findViewById(R.id.img)
}
