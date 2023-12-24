package ru.trading_company.tc_v16.controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.trading_company.tc_v16.R
import ru.trading_company.tc_v16.adapters.PurchaseAdapter
import ru.trading_company.tc_v16.adapters.ShipmentAdapter
import ru.trading_company.tc_v16.models.Purchase
import ru.trading_company.tc_v16.models.Shipment
import ru.trading_company.tc_v16.repositories.PurchaseRepository
import ru.trading_company.tc_v16.repositories.ShipmentRepository

class ShipmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shipment)

        val addBtn: Button = findViewById(R.id.createShipmentBtn)
        val backBtn: Button = findViewById(R.id.backShipmentBtn)
        val listShipment: RecyclerView = findViewById(R.id.shipmentList)
        var shipmentItem = arrayListOf<Shipment>()

        lifecycleScope.launch(Dispatchers.IO) {
            shipmentItem = ShipmentRepository().get() as ArrayList<Shipment>

            withContext(Dispatchers.Main){
                listShipment.layoutManager = LinearLayoutManager(applicationContext)
                listShipment.adapter = ShipmentAdapter(shipmentItem, applicationContext)
            }
        }

        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)

            startActivity(intent)
        }

        addBtn.setOnClickListener {
            val intent = Intent(this, SelectShopActivity::class.java)

            startActivity((intent))
        }
    }
}