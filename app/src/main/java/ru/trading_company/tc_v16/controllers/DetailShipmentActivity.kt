package ru.trading_company.tc_v16.controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.trading_company.tc_v16.R
import ru.trading_company.tc_v16.adapters.PurchaseListAdapter
import ru.trading_company.tc_v16.adapters.ShipmentListAdapter
import ru.trading_company.tc_v16.dialogs.DeletePurchaseDialog
import ru.trading_company.tc_v16.models.Purchase_List
import ru.trading_company.tc_v16.models.Shipment_List
import ru.trading_company.tc_v16.repositories.Purchase_ListRepository
import ru.trading_company.tc_v16.repositories.Shipment_ListRepository

class DetailShipmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_shipment)

        val view = findViewById<View>(android.R.id.content) as ViewGroup
        val deleteBtn: Button = findViewById(R.id.deleteShipmentBtn)
        val backBtn: Button = findViewById(R.id.backDetailShipmentBtn)
        val listProduct: RecyclerView = findViewById(R.id.productList)

        var shipmentListItem = arrayListOf<Shipment_List>()
        val shipmentId = intent.getIntExtra("shipmentId", 1)

        lifecycleScope.launch(Dispatchers.IO) {
            shipmentListItem = Shipment_ListRepository().getByShipment(shipmentId) as ArrayList<Shipment_List>

            withContext(Dispatchers.Main){
                listProduct.layoutManager = LinearLayoutManager(applicationContext)
                listProduct.adapter = ShipmentListAdapter(shipmentListItem, applicationContext)
            }
        }

        backBtn.setOnClickListener {
            val intent = Intent(this, PurchaseActivity::class.java)

            startActivity(intent)
        }

        deleteBtn.setOnClickListener {
            DeletePurchaseDialog(shipmentId!!, view).show(supportFragmentManager, "DELETESHIPMENT")
        }
    }
}