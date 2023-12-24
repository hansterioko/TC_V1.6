package ru.trading_company.tc_v16.controllers

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.publishinghousekotlin.basics.Messages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.trading_company.tc_v16.DTOmodels.PurchaseDTO
import ru.trading_company.tc_v16.DTOmodels.Purchase_ListDTO
import ru.trading_company.tc_v16.R
import ru.trading_company.tc_v16.adapters.AddProductPurchaseAdapter
import ru.trading_company.tc_v16.models.Company
import ru.trading_company.tc_v16.models.Product
import ru.trading_company.tc_v16.models.Purchase
import ru.trading_company.tc_v16.repositories.ProductRepository
import ru.trading_company.tc_v16.repositories.PurchaseRepository
import java.time.LocalDate

class SelectPurchaseProductActivity : AppCompatActivity() {
//    private var message = Messages()
//    private val company: Company = intent.getSerializableExtra("company") as Company
//    private var purchase: Purchase = Purchase(0, 0, LocalDate.now(), company)

    lateinit var selectProductList: List<Product>
    lateinit var selectProductCount: List<Int>
    var iniLists: Boolean = false
    var isSelectData: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_purchase_product)

        val view = findViewById<View>(android.R.id.content) as ViewGroup
        val company: Company = intent.getSerializableExtra("company") as Company
        var purchase = Purchase(0, 0, LocalDate.now(), company)

        val dateText: TextView = findViewById(R.id.datePurchaseText)
        val dateBtn: Button = findViewById(R.id.selectPurchaseDate)
        val addBtn: Button = findViewById(R.id.addPurchaseBtn)

        val productList: RecyclerView = findViewById(R.id.productSelectList)
        var productItems = arrayListOf<Product>()

        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar


        // Иконка назад на тулбаре
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            val intent = Intent(this, SelectCompanyActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

            startActivity(intent)
        })

        lifecycleScope.launch(Dispatchers.IO) {
            productItems = ProductRepository().getByCompany(company.id) as ArrayList<Product>

            withContext(Dispatchers.Main){
                productList.layoutManager = LinearLayoutManager(applicationContext)
                productList.adapter = AddProductPurchaseAdapter(productItems, applicationContext, { getProductList(it) }, { getCountProduct(it) })
            }
        }

        val year = LocalDate.now().year
        val month = LocalDate.now().month.value - 1
        val day = LocalDate.now().dayOfMonth

        dateBtn.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, {_, selectedYear, selectedMonth, selectedDay ->

                purchase!!.date = LocalDate.of(selectedYear,selectedMonth + 1,selectedDay)
                dateText.text = "Выбранная дата: " + purchase.date.toString()
                isSelectData = true

            },year,month,day)

            datePickerDialog.show()
        }

        addBtn.setOnClickListener{
            if (!iniLists){
                Toast.makeText(this, "Выберите товары", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            var purchasePrice: Int = 0
            var purchase_lists = arrayListOf<Purchase_ListDTO>()

            if (selectProductCount.size == 0){
                Toast.makeText(this, "Выберите товары", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isSelectData){
                Toast.makeText(this, "Выберите дату", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            for (i in 0..selectProductCount.size - 1){
                if (selectProductCount.get(i) == 0){
                    Toast.makeText(this, "Товару " + selectProductList.get(i).name + " укажите кол-во", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                purchasePrice += selectProductList.get(i).price * selectProductCount.get(i)
            }
            purchase.price = purchasePrice

            for (i in 0..selectProductCount.size - 1){
                Log.i("ADDINGPUR", "добавление")
                purchase_lists.add(Purchase_ListDTO(0, selectProductList.get(i), selectProductCount.get(i)))
                Log.e("COUNINGPROD", purchase_lists.size.toString())
            }

            addBtn.isEnabled = false

            lifecycleScope.launch(Dispatchers.IO) {
                Messages().showSuccess("Закупка добавлена", view)
                PurchaseRepository().add(PurchaseDTO(0, purchasePrice, purchase.date, purchase_lists, purchase.company))

                delay(600)

                val intent = Intent(this@SelectPurchaseProductActivity, PurchaseActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

                startActivity(intent)
            }
        }
    }

    fun getProductList(list: List<Product>){
        selectProductList = list
        iniLists = true
    }

    fun getCountProduct(list: List<Int>){
        selectProductCount = list
    }
}