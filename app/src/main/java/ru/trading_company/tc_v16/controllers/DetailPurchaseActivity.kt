package ru.trading_company.tc_v16.controllers

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.trading_company.tc_v16.R
import ru.trading_company.tc_v16.adapters.PurchaseListAdapter
import ru.trading_company.tc_v16.dialogs.DeletePurchaseDialog
import ru.trading_company.tc_v16.models.Purchase_List
import ru.trading_company.tc_v16.repositories.Purchase_ListRepository


class DetailPurchaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_purchase)

        val view = findViewById<View>(android.R.id.content) as ViewGroup
        val deleteBtn: Button = findViewById(R.id.deletePurchaseBtn)
        val backBtn: Button = findViewById(R.id.backDetailPurchaseBtn)
        val listProduct: RecyclerView = findViewById(R.id.productList)

        var purchaseListItem = arrayListOf<Purchase_List>()
        val purchaseId = intent.getIntExtra("purchaseId", 1)

        lifecycleScope.launch(Dispatchers.IO) {
            purchaseListItem = Purchase_ListRepository().getByPurchase(purchaseId) as ArrayList<Purchase_List>

            withContext(Dispatchers.Main){
                listProduct.layoutManager = LinearLayoutManager(applicationContext)
                listProduct.adapter = PurchaseListAdapter(purchaseListItem, applicationContext)
            }
        }

        backBtn.setOnClickListener {
            val intent = Intent(this, PurchaseActivity::class.java)

            startActivity(intent)
        }

        deleteBtn.setOnClickListener {
            DeletePurchaseDialog(purchaseId!!, view).show(supportFragmentManager, "DELETEPURCHASE")
        }
    }
}