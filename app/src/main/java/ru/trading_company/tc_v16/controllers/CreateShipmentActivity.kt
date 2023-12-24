package ru.trading_company.tc_v16.controllers

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
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
import ru.trading_company.tc_v16.DTOmodels.ShipmentDTO
import ru.trading_company.tc_v16.DTOmodels.Shipment_ListDTO
import ru.trading_company.tc_v16.R
import ru.trading_company.tc_v16.adapters.CreateShipmentAdapter
import ru.trading_company.tc_v16.models.Product
import ru.trading_company.tc_v16.models.Shipment
import ru.trading_company.tc_v16.models.Shop
import ru.trading_company.tc_v16.repositories.ProductRepository
import ru.trading_company.tc_v16.repositories.PurchaseRepository
import ru.trading_company.tc_v16.repositories.ShipmentRepository
import java.time.LocalDate

class CreateShipmentActivity : AppCompatActivity() {
    lateinit var selectProductList: List<Product>
    lateinit var selectProductCount: List<Int>
    lateinit var selectProductPrice: List<Int>
    var iniLists: Boolean = false
    var isSelectData: Boolean = false
    var discount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_shipment)

        val view = findViewById<View>(android.R.id.content) as ViewGroup
        val shop: Shop = intent.getSerializableExtra("shop") as Shop
        var shipment = Shipment(0, 0, LocalDate.now(), 0, shop)

        val discountText: TextView = findViewById(R.id.discountShipmentText)
        val dateBtn: Button = findViewById(R.id.selectShipmentDate)
        val addBtn: Button = findViewById(R.id.addShipmentBtn)
        val seekDiscount: SeekBar = findViewById(R.id.seekBarDiscount)

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
            productItems = ProductRepository().get() as ArrayList<Product>

            withContext(Dispatchers.Main) {
                productList.layoutManager = LinearLayoutManager(applicationContext)
                var newProductList = arrayListOf<Product>()

                for (item: Product in productItems) {
                    if (item.countOnWarehouse > 0) {
                        newProductList.add(item)
                    }
                }
                productList.adapter = CreateShipmentAdapter(
                    newProductList,
                    applicationContext,
                    { getProductList(it) },
                    { getCountProduct(it) },
                    { getPriceProduct(it) })
            }
        }

        val year = LocalDate.now().year
        val month = LocalDate.now().month.value - 1
        val day = LocalDate.now().dayOfMonth

        dateBtn.setOnClickListener {
            val datePickerDialog =
                DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->

                    shipment!!.date = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                    dateBtn.text = shipment.date.toString()
                    isSelectData = true

                }, year, month, day)

            datePickerDialog.show()
        }

        seekDiscount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                discountText.text = "Скидка " + progress + "%"
                discount = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // you can probably leave this empty
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // you can probably leave this empty
            }
        })

        addBtn.setOnClickListener {
            if (!iniLists) {
                Toast.makeText(this, "Выберите товары", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            var shipmentPrice: Int = 0
            var shipment_lists = arrayListOf<Shipment_ListDTO>()

            if (selectProductCount.size == 0) {
                Toast.makeText(this, "Выберите товары", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isSelectData) {
                Toast.makeText(this, "Выберите дату", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            for (i in 0..selectProductCount.size - 1) {
                if (selectProductCount.get(i) == 0) {
                    Toast.makeText(
                        this,
                        "Товару " + selectProductList.get(i).name + " укажите кол-во",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }
                shipmentPrice += (selectProductPrice.get(i) * selectProductCount.get(i)) * (100 - discount) / 100
            }
            shipment.price = if (shipmentPrice == 0){
                1
            }
            else{
                shipmentPrice
            }

            for (i in 0..selectProductCount.size - 1) {
//                Log.i("ADDINGPUR", "добавление")
                shipment_lists.add(
                    Shipment_ListDTO(
                        0,
                        selectProductList.get(i),
                        selectProductCount.get(i),
                        selectProductPrice.get(i)
                    )
                )
             //   Log.e("COUNINGPROD", shipment_lists.size.toString())
            }

            addBtn.isEnabled = false

            lifecycleScope.launch(Dispatchers.IO) {
                Messages().showSuccess("Поставка добавлена", view)
                ShipmentRepository().add(
                    ShipmentDTO(
                        0,
                        shipment.price,
                        shipment.date,
                        shipment_lists,
                        discount,
                        shipment.shop
                    )
                )

                delay(600)

                val intent = Intent(this@CreateShipmentActivity, ShipmentActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

                startActivity(intent)
            }
        }
    }

    fun getProductList(list: List<Product>) {
        selectProductList = list
        iniLists = true
    }

    fun getCountProduct(list: List<Int>) {
        selectProductCount = list
    }

    fun getPriceProduct(list: List<Int>) {
        selectProductPrice = list
    }
}