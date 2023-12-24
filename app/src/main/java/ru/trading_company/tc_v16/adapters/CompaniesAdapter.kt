package ru.trading_company.tc_v16.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import ru.trading_company.tc_v16.R
import ru.trading_company.tc_v16.controllers.CompaniesActivity
import ru.trading_company.tc_v16.dialogs.DeleteCompanyDialog
import ru.trading_company.tc_v16.models.Company


class CompaniesAdapter (var companies: List<Company>, val context: Context, val view: ViewGroup, val activity: CompaniesActivity) : RecyclerView.Adapter<CompaniesAdapter.MyViewHolder>() {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){

        val title: TextView = view.findViewById(R.id.titleCompanyText)
        val city: TextView = view.findViewById(R.id.cityCompanyText)
        val street: TextView = view.findViewById(R.id.streetCompanyText)
        val house: TextView = view.findViewById(R.id.houseCompanyText)


        val deleteBtn: Button = view.findViewById(R.id.deleteCompanyBtn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.company_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return companies.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.title.text = companies[position].name
        holder.city.text = "Город: " + companies[position].city
        holder.street.text = "Улица: " + companies[position].street
        holder.house.text = "Дом: " + companies[position].house

        holder.deleteBtn.setOnClickListener {
            DeleteCompanyDialog(companies[position].id!!, view).show(activity.supportFragmentManager, "DELETECOMPANY")
        }
    }
}