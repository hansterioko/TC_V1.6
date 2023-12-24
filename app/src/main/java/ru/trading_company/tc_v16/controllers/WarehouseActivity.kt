package ru.trading_company.tc_v16.controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.trading_company.tc_v16.R
import ru.trading_company.tc_v16.adapters.SelectCompanyAdapter
import ru.trading_company.tc_v16.adapters.WarehouseAdapter
import ru.trading_company.tc_v16.models.Company
import ru.trading_company.tc_v16.models.Product
import ru.trading_company.tc_v16.repositories.CompanyRepository
import ru.trading_company.tc_v16.repositories.ProductRepository

class WarehouseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_warehouse)

        val addBtn: Button = findViewById(R.id.addNewProductBtn)
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar


        // Иконка назад на тулбаре
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            lifecycleScope.launch(Dispatchers.IO) {
                startActivity(intent)
            }
        })

        val listProduct: RecyclerView = findViewById(R.id.productListView)
        var productItem = arrayListOf<Product>()

        lifecycleScope.launch(Dispatchers.IO) {
            productItem = ProductRepository().get() as ArrayList<Product>

            withContext(Dispatchers.Main){
                listProduct.layoutManager = LinearLayoutManager(applicationContext)
                listProduct.adapter = WarehouseAdapter(productItem, applicationContext)
            }
        }

        addBtn.setOnClickListener {
            val intent = Intent(this, CreateProductActivity::class.java)

            startActivity(intent)
        }
    }
}