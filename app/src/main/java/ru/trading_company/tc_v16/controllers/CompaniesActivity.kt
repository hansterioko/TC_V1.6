package ru.trading_company.tc_v16.controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.trading_company.tc_v16.R
import ru.trading_company.tc_v16.adapters.CompaniesAdapter
import ru.trading_company.tc_v16.adapters.WarehouseAdapter
import ru.trading_company.tc_v16.models.Company
import ru.trading_company.tc_v16.models.Product
import ru.trading_company.tc_v16.repositories.CompanyRepository
import ru.trading_company.tc_v16.repositories.ProductRepository

class CompaniesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_companies)

        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar

        // Иконка назад на тулбаре
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            lifecycleScope.launch(Dispatchers.IO) {
                startActivity(intent)
            }
        })


        val view = findViewById<View>(android.R.id.content) as ViewGroup

        val addBtn: Button = findViewById(R.id.addNewCompanyBtn)

        val listCompany: RecyclerView = findViewById(R.id.companiesListView)
        var companyItem = arrayListOf<Company>()

        lifecycleScope.launch(Dispatchers.IO) {
            companyItem = CompanyRepository().get() as ArrayList<Company>

            withContext(Dispatchers.Main){
                listCompany.layoutManager = LinearLayoutManager(applicationContext)
                listCompany.adapter = CompaniesAdapter(companyItem, applicationContext, view, this@CompaniesActivity)
            }
        }

        addBtn.setOnClickListener {
            val intent = Intent(this@CompaniesActivity, CreateCompanyActivity::class.java)

            startActivity(intent)
        }
    }
}