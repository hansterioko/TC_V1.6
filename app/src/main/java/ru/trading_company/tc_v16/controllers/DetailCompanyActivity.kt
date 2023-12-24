package ru.trading_company.tc_v16.controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import ru.trading_company.tc_v16.R
import ru.trading_company.tc_v16.models.Company

class DetailCompanyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_company)

        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        val title: TextView = findViewById(R.id.titleCompanyText)
        val city: TextView = findViewById(R.id.cityCompanyText)
        val street: TextView = findViewById(R.id.streetCompanyText)
        val house: TextView = findViewById(R.id.houseCompanyText)

        // Иконка назад на тулбаре
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            this.finish()
        })

        val company: Company = intent.getSerializableExtra("company") as Company

        title.text = company.name
        city.text = "Город: " + company.city
        street.text = "Улица: " + company.street
        house.text = "Дом: " + company.house

    }
}