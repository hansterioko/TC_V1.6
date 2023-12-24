package ru.trading_company.tc_v16.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.publishinghousekotlin.basics.FileWorker
import ru.trading_company.tc_v16.R
import ru.trading_company.tc_v16.controllers.DetailProductActivity
import ru.trading_company.tc_v16.models.Product
import ru.trading_company.tc_v16.models.Purchase_List


class PurchaseListAdapter (var purchaseList: List<Purchase_List>, val context: Context) : RecyclerView.Adapter<PurchaseListAdapter.MyViewHolder>(){
    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.productItemTitle)
        val text: TextView = view.findViewById(R.id.productItemText)
        val count: TextView = view.findViewById(R.id.productItemCount)
        val detailBtn: Button = view.findViewById(R.id.detailProductItemBtn)
        val image: ImageView = view.findViewById(R.id.productItemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return purchaseList.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = purchaseList[position].product.name
        holder.text.text = purchaseList[position].product.characteristic
        holder.count.text = "Кол-во закуплено: " + purchaseList[position].count.toString()
        holder.image.setImageBitmap(FileWorker().getBitmap(purchaseList[position].product.photo))

        holder.detailBtn.setOnClickListener {
            val intent = Intent(context, DetailProductActivity::class.java)

            intent.putExtra("productId", purchaseList[position].product.id)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            context.startActivity(intent)
        }
    }
}