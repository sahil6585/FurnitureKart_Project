package com.example.furniturekart.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.furniturekart.Activity.CartviewActivity
import com.example.furniturekart.Activity.FullScreenProductActivity
import com.example.furniturekart.Activity.WishListViewActivity
import com.example.furniturekart.Client.ApiClient
import com.example.furniturekart.Interface.ApiInterface
import com.example.furniturekart.Model.ProductDetailModel
import com.example.furniturekart.Model.WishlistModel
import com.example.furniturekart.R
import com.github.chrisbanes.photoview.PhotoView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WishlistAdapter(var context: Context, var list: List<WishlistModel>): RecyclerView.Adapter<MyView3>()
{

    lateinit var apiInterface: ApiInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView3 {
        var  layoutInflater= LayoutInflater.from(parent.context)
        var view = layoutInflater.inflate(R.layout.wishlist_design,parent,false)
        return MyView3(view)
    }

    override fun getItemCount(): Int {
        return list.size

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyView3, @SuppressLint("RecyclerView") position: Int) {
        holder.pbrand.setText(list.get(position).p_brand)
        holder.pname.setText(list.get(position).p_name)
        holder.pprice.setText(list.get(position).p_mrp.toString())
        var c_id =list.get(position).c_id
        Picasso.get().load(list.get(position).p_image).into(holder.pimage)

        apiInterface =  ApiClient.getapiclient().create(ApiInterface::class.java)

        holder.deletewish.setOnClickListener {

           // Toast.makeText(context, ""+list.get(position).id, Toast.LENGTH_SHORT).show()

            var call:Call<Void> = apiInterface.deletewishlistdata(list.get(position).id)
            call.enqueue(object : Callback<Void>{
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    var i =Intent(context,WishListViewActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(i)
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(context,"DELETE AND ADD TO CART FAIL",Toast.LENGTH_LONG).show()
                }
            } )
        }

        holder.addtocart.setOnClickListener {
            var call: Call<Void> = apiInterface.cartdata(c_id, list.get(position).p_brand, list.get(position).p_name ,list.get(position).p_mrp, list.get(position).p_image)

            call.enqueue(object: Callback<Void> {
                @SuppressLint("SuspiciousIndentation")
                override fun onResponse(call: Call<Void>, response: Response<Void>)
                {
                    Toast.makeText(context,"ADDED TO CART",Toast.LENGTH_LONG).show()

                    var call:Call<Void> = apiInterface.deletewishlistdata(list.get(position).id)
                        call.enqueue(object : Callback<Void>{
                            @SuppressLint("NotifyDataSetChanged")
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                var i =Intent(context,CartviewActivity::class.java)
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                context.startActivity(i)
                            }
                            override fun onFailure(call: Call<Void>, t: Throwable)
                            {
                                Toast.makeText(context,"DELETE AND ADD TO CART FAIL",Toast.LENGTH_LONG).show()
                            }
                        } )
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(context,"ADD TO CART FAIL",Toast.LENGTH_LONG).show()
                }
            })

        }
    }
}

class MyView3(Itemview: View):RecyclerView.ViewHolder(Itemview)
{
    var pimage: ImageView    = Itemview.findViewById(R.id.pimage)
    var pbrand: TextView = Itemview.findViewById(R.id.pbrand)
    var pname: TextView = Itemview.findViewById(R.id.pname)
    var pprice: TextView = Itemview.findViewById(R.id.pprice)
    var addtocart: Button = Itemview.findViewById(R.id.addtocart)
    var deletewish: Button = Itemview.findViewById(R.id.delete)


}
