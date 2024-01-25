package com.example.furniturekart.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.furniturekart.Activity.FullScreenProductActivity
import com.example.furniturekart.Model.CategoryModel
import com.example.furniturekart.Model.ProductDetailModel
import com.example.furniturekart.R
import com.squareup.picasso.Picasso

class ProductAdapter(var context: Context, var list: List<ProductDetailModel>): RecyclerView.Adapter<MyView2>() {

/*
    var FilterList = ArrayList<ProductDetailModel>()

    init {
        FilterList.addAll(list)
    }
*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView2 {
        var layoutInflater = LayoutInflater.from(parent.context)
        var view = layoutInflater.inflate(R.layout.product_list_design, parent, false)
        return MyView2(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyView2, position: Int) {
        holder.pbrand.setText(list.get(position).p_brand)
        holder.pinfo.setText(list.get(position).p_info)
        holder.pprice.setText(list.get(position).p_mrp.toString())
        holder.dis.setText(list.get(position).p_per.toString())
        holder.fprice.setText(list.get(position).fake_mrp.toString())
        Picasso.get().load(list.get(position).p_image).into(holder.pimage)

        holder.fprice.setPaintFlags(holder.fprice.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)

        holder.itemView.setOnClickListener {

            var i = Intent(context, FullScreenProductActivity::class.java)
            i.putExtra("id", list.get(position).id)
            i.putExtra("c_id", list.get(position).c_id)
            i.putExtra("name", list.get(position).p_name)
            i.putExtra("mrp", list.get(position).p_mrp)
            i.putExtra("p_per", list.get(position).p_per)
            i.putExtra("fake_mrp", list.get(position).fake_mrp)
            i.putExtra("info", list.get(position).p_info)
            i.putExtra("image", list.get(position).p_image)
            i.putExtra("brand", list.get(position).p_brand)
            i.putExtra("color", list.get(position).p_color)
            i.putExtra("material", list.get(position).p_material)
            i.putExtra("weight", list.get(position).p_weight)
            i.putExtra("no_of_item", list.get(position).number_of_item)
            i.putExtra("no_of_pieces", list.get(position).number_of_pieces)
            i.putExtra("menufacturer", list.get(position).p_menufacturer)
            i.putExtra("country", list.get(position).p_country)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(i)

        }

        /*  val item = FilterList[position]
        holder.pbrand.text = item.p_brand
        Picasso.get().load(item.p_image).into(holder.pimage)
    }
    fun filter(query: String)
    {
       FilterList.clear()
        if(query.isEmpty())
        {
           FilterList.addAll(list)
        }
        else
        {
            val lower = query.toLowerCase()

            for(item in list)
            {
                if(item.p_brand.toLowerCase().contains(lower))
                {
                    FilterList.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }*/
    }
}
class MyView2(Itemview: View):RecyclerView.ViewHolder(Itemview)
{
    var pimage: ImageView = Itemview.findViewById(R.id.pimage)
    var pbrand: TextView = Itemview.findViewById(R.id.pbrand)
    var pinfo: TextView = Itemview.findViewById(R.id.pinfo)
    var pprice: TextView = Itemview.findViewById(R.id.pprice)
    var fprice: TextView = Itemview.findViewById(R.id.fprice)
    var dis: TextView = Itemview.findViewById(R.id.dicount)


}
