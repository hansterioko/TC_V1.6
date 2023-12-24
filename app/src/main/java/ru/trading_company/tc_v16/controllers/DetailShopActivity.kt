package ru.trading_company.tc_v16.controllers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import ru.trading_company.tc_v16.R
import ru.trading_company.tc_v16.models.Company
import ru.trading_company.tc_v16.models.Shop

class DetailShopActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_shop)

        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        val title: TextView = findViewById(R.id.titleShopText)
        val city: TextView = findViewById(R.id.cityShopText)
        val street: TextView = findViewById(R.id.streetShopText)
        val house: TextView = findViewById(R.id.houseShopText)

        // Иконка назад на тулбаре
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            this.finish()
        })

        val shop: Shop = intent.getSerializableExtra("shop") as Shop

        title.text = shop.name
        city.text = "Город: " + shop.city
        street.text = "Улица: " + shop.street
        house.text = "Дом: " + shop.house
    }
}