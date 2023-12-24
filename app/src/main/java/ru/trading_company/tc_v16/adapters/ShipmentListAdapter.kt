package ru.trading_company.tc_v16.adapters

import android.content.Context
import android.content.Intent
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
import ru.trading_company.tc_v16.models.Shipment_List

class ShipmentListAdapter (var shipmentList: List<Shipment_List>, val context: Context) : RecyclerView.Adapter<ShipmentListAdapter.MyViewHolder>(){
    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.productItemTitle)
        val text: TextView = view.findViewById(R.id.productItemText)
        val price: TextView = view.findViewById(R.id.productItemPrice)
        val count: TextView = view.findViewById(R.id.productItemCount)
        val detailBtn: Button = view.findViewById(R.id.detailProductItemBtn)
        val image: ImageView = view.findViewById(R.id.productItemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_shipment_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return shipmentList.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = shipmentList[position].product.name
        holder.text.text = shipmentList[position].product.characteristic
        holder.price.text = "Цена продажи: " + shipmentList[position].price.toString()
        holder.count.text = "Поставлено: " + shipmentList[position].count.toString() + " ед"
        holder.image.setImageBitmap(FileWorker().getBitmap(shipmentList[position].product.photo))

        holder.detailBtn.setOnClickListener {
            val intent = Intent(context, DetailProductActivity::class.java)

            intent.putExtra("productId", shipmentList[position].product.id)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            context.startActivity(intent)
        }
    }
}