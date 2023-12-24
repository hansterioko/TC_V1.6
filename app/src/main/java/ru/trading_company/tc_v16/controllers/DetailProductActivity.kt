package ru.trading_company.tc_v16.controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.publishinghousekotlin.basics.FileWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.trading_company.tc_v16.R
import ru.trading_company.tc_v16.adapters.PurchaseListAdapter
import ru.trading_company.tc_v16.dialogs.DeleteProductDialog
import ru.trading_company.tc_v16.dialogs.DeletePurchaseDialog
import ru.trading_company.tc_v16.models.Product
import ru.trading_company.tc_v16.repositories.ProductRepository

class DetailProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)

        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar


        // Иконка назад на тулбаре
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            finish()
        })

        val id: Int = intent.getIntExtra("productId", 1)
//        Log.e("IDDDDD", id.toString())

        var product: Product = Product(0, "", 2, "", "", "", 0, 0, null, "")

        val view = findViewById<View>(android.R.id.content) as ViewGroup
        val title: TextView = findViewById(R.id.titleProduct)
        val desc: TextView = findViewById(R.id.descProduct)
        val companyName: TextView = findViewById(R.id.companyProduct)
        val unitText: TextView = findViewById(R.id.unitProduct)
        val vat: TextView = findViewById(R.id.vatProduct)
        val price: TextView = findViewById(R.id.purchasePriceProduct)
        val count: TextView = findViewById(R.id.countProduct)
        val category: TextView = findViewById(R.id.categoryProduct)
        val deleteBtn: Button = findViewById(R.id.deleteProductBtn)

        val image: ImageView = findViewById(R.id.productView)

        if (!intent.getBooleanExtra("isWarehouse", false)){
            deleteBtn.isEnabled = false
        }

        lifecycleScope.launch(Dispatchers.IO) {
            product = ProductRepository().getOne(id)!!

            withContext(Dispatchers.Main){
                title.text = product.name
                desc.text = product.characteristic
                companyName.text = "От компании: " + product.company!!.name
                unitText.text = "Единица изм.: " + product.unit
                vat.text = "НДС: " + product.vat.toString()
                price.text = "Цена: " + product.price.toString()
                count.text = "Кол-во: " + product.countOnWarehouse.toString()
                category.text = "Категория: " + product.category

                image.setImageBitmap(FileWorker().getBitmap(product.photo))
            }
        }

        deleteBtn.setOnClickListener {
            DeleteProductDialog(id!!, view).show(supportFragmentManager, "DELETEPRODUCT")
        }
    }
}