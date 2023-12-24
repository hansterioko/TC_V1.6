package ru.trading_company.tc_v16.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.trading_company.tc_v16.R
import ru.trading_company.tc_v16.controllers.DetailShipmentActivity
import ru.trading_company.tc_v16.models.Shipment
import java.time.format.DateTimeFormatter

class ShipmentAdapter (var shipments: List<Shipment>, val context: Context) : RecyclerView.Adapter<ShipmentAdapter.MyViewHolder>() {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val date: TextView = view.findViewById(R.id.shipmentItemTitle)
        val forShop: TextView = view.findViewById(R.id.shipmentItemText)
        val price: TextView = view.findViewById(R.id.shipmentItemPrice)
        val discount: TextView = view.findViewById(R.id.shipmentItemDiscount)

        val detailBtn: Button = view.findViewById(R.id.detailShipmentItemBtn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shipment_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return shipments.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.date.text = shipments[position].date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        holder.forShop.text = "В магазин: " + shipments[position].shop.name
        holder.price.text = "Стоимость: " + shipments[position].price.toString()
        holder.discount.text = "Скидка для магазина: " + shipments[position].discount + "%"

        holder.detailBtn.setOnClickListener {
            val intent = Intent(context, DetailShipmentActivity::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("shipmentId", shipments[position].id)

            context.startActivity(intent)
        }
    }
}