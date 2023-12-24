package ru.trading_company.tc_v16.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ContentInfoCompat.Flags
import androidx.recyclerview.widget.RecyclerView
import com.example.publishinghousekotlin.basics.FileWorker
import ru.trading_company.tc_v16.R
import ru.trading_company.tc_v16.controllers.DetailProductActivity
import ru.trading_company.tc_v16.models.Product

class WarehouseAdapter (var products: List<Product>, val context: Context) : RecyclerView.Adapter<WarehouseAdapter.MyViewHolder>() {
    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){

        val title: TextView = view.findViewById(R.id.productItemTitle)
        val desc: TextView = view.findViewById(R.id.productItemText)
        val price: TextView = view.findViewById(R.id.productItemPrice)
        val image: ImageView = view.findViewById(R.id.productItemView)
        val count: TextView = view.findViewById(R.id.countOnWarehouseText)

        val detailBtn: Button = view.findViewById(R.id.detailProductItemBtn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.warehouse_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.title.text = products[position].name
        holder.desc.text = products[position].characteristic
        holder.price.text = "Цена закупки: " + products[position].price.toString()
        holder.count.text = "Кол-во: " + products[position].countOnWarehouse.toString()
        holder.image.setImageBitmap(FileWorker().getBitmap(products[position].photo))

        holder.detailBtn.setOnClickListener {
            val intent = Intent(context, DetailProductActivity::class.java)
            intent.putExtra("productId", products[position].id)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("isWarehouse", true)

            context.startActivity(intent)
        }
    }
}