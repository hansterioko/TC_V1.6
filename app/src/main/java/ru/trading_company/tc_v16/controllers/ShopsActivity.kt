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
import ru.trading_company.tc_v16.adapters.ShopsAdapter
import ru.trading_company.tc_v16.models.Company
import ru.trading_company.tc_v16.models.Shop
import ru.trading_company.tc_v16.repositories.CompanyRepository
import ru.trading_company.tc_v16.repositories.ShopRepository

class ShopsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar

        // Иконка назад на тулбаре
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            lifecycleScope.launch(Dispatchers.IO) {
                startActivity(intent)
            }
        })


        val view = findViewById<View>(android.R.id.content) as ViewGroup

        val addBtn: Button = findViewById(R.id.addNewShopBtn)

        val listShop: RecyclerView = findViewById(R.id.shopsListView)
        var shopItem = arrayListOf<Shop>()

        lifecycleScope.launch(Dispatchers.IO) {
            shopItem = ShopRepository().get() as ArrayList<Shop>

            withContext(Dispatchers.Main){
                listShop.layoutManager = LinearLayoutManager(applicationContext)
                listShop.adapter = ShopsAdapter(shopItem, applicationContext, view, this@ShopsActivity)
            }
        }

        addBtn.setOnClickListener {
            val intent = Intent(this@ShopsActivity, CreateShopActivity::class.java)

            startActivity(intent)
        }
    }
}