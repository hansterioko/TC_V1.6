package ru.trading_company.tc_v16.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.trading_company.tc_v16.R
import ru.trading_company.tc_v16.controllers.ShopsActivity
import ru.trading_company.tc_v16.dialogs.DeleteCompanyDialog
import ru.trading_company.tc_v16.dialogs.DeleteShopDialog
import ru.trading_company.tc_v16.models.Shop

class ShopsAdapter (var shops: List<Shop>, val context: Context, val view: ViewGroup, val activity: ShopsActivity) : RecyclerView.Adapter<ShopsAdapter.MyViewHolder>() {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){

        val title: TextView = view.findViewById(R.id.titleShopText)
        val city: TextView = view.findViewById(R.id.cityShopText)
        val street: TextView = view.findViewById(R.id.streetShopText)
        val house: TextView = view.findViewById(R.id.houseShopText)


        val deleteBtn: Button = view.findViewById(R.id.deleteShopBtn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shop_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return shops.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.title.text = shops[position].name
        holder.city.text = "Город: " + shops[position].city
        holder.street.text = "Улица: " + shops[position].street
        holder.house.text = "Дом: " + shops[position].house

        holder.deleteBtn.setOnClickListener {
            DeleteShopDialog(shops[position].id!!, view).show(activity.supportFragmentManager, "DELETESHOP")
        }
    }
}