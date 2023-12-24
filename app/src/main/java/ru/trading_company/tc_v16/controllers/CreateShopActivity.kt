package ru.trading_company.tc_v16.controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.publishinghousekotlin.basics.Listener
import com.example.publishinghousekotlin.basics.Messages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.trading_company.tc_v16.R
import ru.trading_company.tc_v16.databinding.ActivityCreateCompanyBinding
import ru.trading_company.tc_v16.databinding.ActivityCreateShopBinding
import ru.trading_company.tc_v16.models.Company
import ru.trading_company.tc_v16.models.Shop
import ru.trading_company.tc_v16.repositories.CompanyRepository
import ru.trading_company.tc_v16.repositories.ShopRepository

class CreateShopActivity : AppCompatActivity() {

    private lateinit var createShopBinding: ActivityCreateShopBinding
    private lateinit var view: ViewGroup
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_shop)

        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar

        // Иконка назад на тулбаре
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            val intent = Intent(this, ShopsActivity::class.java)
            lifecycleScope.launch(Dispatchers.IO) {
                startActivity(intent)
            }
        })

        createShopBinding = ActivityCreateShopBinding.inflate(layoutInflater)
        setContentView(createShopBinding.root)

        view = findViewById<View>(android.R.id.content) as ViewGroup

        setListeners()

        createShopBinding.addNewShopBtn.setOnClickListener {
            save()
        }
    }

    fun setListeners(){
        val listener = Listener()

        listener.nameListener(createShopBinding.titleText, createShopBinding.titleContainer)
        listener.nameListener(createShopBinding.cityText, createShopBinding.cityContainer)
        listener.nameListener(createShopBinding.streetText, createShopBinding.streetContainer)
        listener.nameListener(createShopBinding.houseText, createShopBinding.houseContainer)

    }

    private fun save(){
        if (createShopBinding.titleContainer.helperText == null && createShopBinding.cityContainer.helperText == null &&
            createShopBinding.streetContainer.helperText == null && createShopBinding.houseContainer.helperText == null){

            createShopBinding.addNewShopBtn.isEnabled = false

            val shop = Shop(0, createShopBinding.titleText.text.toString(), createShopBinding.cityText.text.toString(),
                createShopBinding.streetText.text.toString(), createShopBinding.houseText.text.toString())

            lifecycleScope.launch(Dispatchers.IO) {
                Messages().showSuccess("Магазин добавлена", view)
                ShopRepository().add(shop)

                delay(600)

                val intent = Intent(this@CreateShopActivity, ShopsActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

                startActivity(intent)

            }

        }
        else{
            Toast.makeText(this, "Не все поля корректно заполнены", Toast.LENGTH_LONG).show()
        }
    }
}