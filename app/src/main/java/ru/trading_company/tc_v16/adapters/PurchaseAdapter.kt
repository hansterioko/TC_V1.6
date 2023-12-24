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
import ru.trading_company.tc_v16.controllers.DetailPurchaseActivity
import ru.trading_company.tc_v16.controllers.PurchaseActivity
import ru.trading_company.tc_v16.models.Purchase
import java.time.format.DateTimeFormatter

class PurchaseAdapter(var purchases: List<Purchase>, val context: Context) : RecyclerView.Adapter<PurchaseAdapter.MyViewHolder>() {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val date: TextView = view.findViewById(R.id.purchaseItemTitle)
        val fromCompany: TextView = view.findViewById(R.id.purchaseItemText)
        val price: TextView = view.findViewById(R.id.purchaseItemPrice)
        val detailBtn: Button = view.findViewById(R.id.detailPurchaseItemBtn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.purchase_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return purchases.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.date.text = purchases[position].date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        holder.fromCompany.text = "От компании: " + purchases[position].company.name
        holder.price.text = "Стоимость: " + purchases[position].price.toString()

        holder.detailBtn.setOnClickListener {
            val intent = Intent(context, DetailPurchaseActivity::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("purchaseId", purchases[position].id)

            context.startActivity(intent)
        }
    }
}