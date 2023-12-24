package ru.trading_company.tc_v16.controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.trading_company.tc_v16.R
import ru.trading_company.tc_v16.adapters.SelectCompanyAdapter
import ru.trading_company.tc_v16.adapters.SelectShopAdapter
import ru.trading_company.tc_v16.models.Company
import ru.trading_company.tc_v16.models.Shop
import ru.trading_company.tc_v16.repositories.CompanyRepository
import ru.trading_company.tc_v16.repositories.ShopRepository

class SelectShopActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_shop)

        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar


        // Иконка назад на тулбаре
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            val intent = Intent(this, ShipmentActivity::class.java)

            startActivity(intent)
        })

        // Для Recycle
        val listShop: RecyclerView = findViewById(R.id.shopSelectList)
        var shopItem = arrayListOf<Shop>()

        lifecycleScope.launch(Dispatchers.IO) {
            shopItem = ShopRepository().get() as ArrayList<Shop>

            withContext(Dispatchers.Main) {
                listShop.layoutManager = LinearLayoutManager(applicationContext)
                listShop.adapter = SelectShopAdapter(shopItem, applicationContext)
            }
        }
    }
}