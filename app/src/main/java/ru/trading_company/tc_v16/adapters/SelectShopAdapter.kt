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
import ru.trading_company.tc_v16.controllers.CreateShipmentActivity
import ru.trading_company.tc_v16.controllers.CreateShopActivity
import ru.trading_company.tc_v16.controllers.DetailShopActivity
import ru.trading_company.tc_v16.controllers.SelectPurchaseProductActivity
import ru.trading_company.tc_v16.models.Shop

class SelectShopAdapter (var shops: List<Shop>, val context: Context) : RecyclerView.Adapter<SelectShopAdapter.MyViewHolder>() {
    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){

        val titleCompany: TextView = view.findViewById(R.id.selectCompanyTitleText)
        val detailBtn: Button = view.findViewById(R.id.detailSelectCompanyBtn)
        val selectBtn: Button = view.findViewById(R.id.selectCompanyBtn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.select_company_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return shops.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.titleCompany.text = shops[position].name

        holder.detailBtn.setOnClickListener {
            val intent = Intent(context, DetailShopActivity::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("shop", shops[position])

            context.startActivity(intent)
        }

        holder.selectBtn.setOnClickListener {
            val intent = Intent(context, CreateShipmentActivity::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("shop", shops[position])

            context.startActivity(intent)
        }
    }
}