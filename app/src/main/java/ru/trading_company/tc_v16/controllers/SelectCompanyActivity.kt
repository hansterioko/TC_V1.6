package ru.trading_company.tc_v16.controllers

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.trading_company.tc_v16.R.*
import ru.trading_company.tc_v16.adapters.SelectCompanyAdapter
import ru.trading_company.tc_v16.models.Company
import ru.trading_company.tc_v16.repositories.CompanyRepository


class SelectCompanyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_select_company)

        val toolbar: Toolbar = findViewById<View>(id.toolbar) as Toolbar


        // Иконка назад на тулбаре
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            val intent = Intent(this, PurchaseActivity::class.java)

            startActivity(intent)
        })

        // Для Recycle
        val listCompany: RecyclerView = findViewById(id.companySelectList)
        var companyItem = arrayListOf<Company>()

        lifecycleScope.launch(Dispatchers.IO) {
            companyItem = CompanyRepository().get() as ArrayList<Company>

            withContext(Dispatchers.Main){
                listCompany.layoutManager = LinearLayoutManager(applicationContext)
                listCompany.adapter = SelectCompanyAdapter(companyItem, applicationContext)
            }
        }
    }
}