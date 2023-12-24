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
import ru.trading_company.tc_v16.models.Purchase
import ru.trading_company.tc_v16.repositories.PurchaseRepository

class PurchaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase)

        val addBtn: Button = findViewById(R.id.createPurchaseBtn)
        val backBtn: Button = findViewById(R.id.backPurchaseBtn)
        val listPurchase: RecyclerView = findViewById(R.id.purchaseList)
        var purchaseItem = arrayListOf<Purchase>()

        lifecycleScope.launch(Dispatchers.IO) {
            purchaseItem = PurchaseRepository().get() as ArrayList<Purchase>

            withContext(Dispatchers.Main){
                listPurchase.layoutManager = LinearLayoutManager(applicationContext)
                listPurchase.adapter = PurchaseAdapter(purchaseItem, applicationContext)
            }
        }

        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)

            startActivity(intent)
        }

        addBtn.setOnClickListener {
            val intent = Intent(this, SelectCompanyActivity::class.java)

            startActivity((intent))
        }
    }
}