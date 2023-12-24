package ru.trading_company.tc_v16.controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import ru.trading_company.tc_v16.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val purchaseBtn: Button = findViewById(R.id.purchaseBtn)
        val shipmentBtn: Button = findViewById(R.id.shipmentBtn)
        val warehouseBtn: Button = findViewById(R.id.warehouseBtn)
        val companiesBtn: Button = findViewById(R.id.skipToCompaniesBtn)
        val shopsBtn: Button = findViewById(R.id.skipToShopsBtn)

        purchaseBtn.setOnClickListener {
            val skipToPurchase = Intent(this, PurchaseActivity::class.java)
            startActivity(skipToPurchase)
        }

        warehouseBtn.setOnClickListener {
            val skipToWarehouse = Intent(this, WarehouseActivity::class.java)
            startActivity(skipToWarehouse)
        }

        shipmentBtn.setOnClickListener {
            val skipToShipment = Intent(this, ShipmentActivity::class.java)
            startActivity(skipToShipment)
        }

        companiesBtn.setOnClickListener {
            val skipToCompanies = Intent(this, CompaniesActivity::class.java)
            startActivity(skipToCompanies)
        }

        shopsBtn.setOnClickListener {
            val skipToShops = Intent(this, ShopsActivity::class.java)
            startActivity(skipToShops)
        }
    }
}