package ru.trading_company.tc_v16.controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.publishinghousekotlin.basics.FileWorker
import com.example.publishinghousekotlin.basics.Listener
import com.example.publishinghousekotlin.basics.Messages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.trading_company.tc_v16.R
import ru.trading_company.tc_v16.databinding.ActivityCreateCompanyBinding
import ru.trading_company.tc_v16.databinding.ActivityCreateProductBinding
import ru.trading_company.tc_v16.models.Company
import ru.trading_company.tc_v16.models.Product
import ru.trading_company.tc_v16.repositories.CompanyRepository
import ru.trading_company.tc_v16.repositories.ProductRepository
import java.io.File

class CreateCompanyActivity : AppCompatActivity() {
    private lateinit var createCompanyBinding: ActivityCreateCompanyBinding
    private lateinit var view: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_company)

        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar

        // Иконка назад на тулбаре
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            val intent = Intent(this, CompaniesActivity::class.java)
            lifecycleScope.launch(Dispatchers.IO) {
                startActivity(intent)
            }
        })

        createCompanyBinding = ActivityCreateCompanyBinding.inflate(layoutInflater)
        setContentView(createCompanyBinding.root)

        view = findViewById<View>(android.R.id.content) as ViewGroup

        setListeners()

        createCompanyBinding.addNewCompanyBtn.setOnClickListener {
            save()
        }

    }

    fun setListeners(){
        val listener = Listener()

        listener.nameListener(createCompanyBinding.titleText, createCompanyBinding.titleContainer)
        listener.nameListener(createCompanyBinding.cityText, createCompanyBinding.cityContainer)
        listener.nameListener(createCompanyBinding.streetText, createCompanyBinding.streetContainer)
        listener.nameListener(createCompanyBinding.houseText, createCompanyBinding.houseContainer)

    }

    private fun save(){
        if (createCompanyBinding.titleContainer.helperText == null && createCompanyBinding.cityContainer.helperText == null &&
            createCompanyBinding.streetContainer.helperText == null && createCompanyBinding.houseContainer.helperText == null){

            createCompanyBinding.addNewCompanyBtn.isEnabled = false

            val company = Company(0, createCompanyBinding.titleText.text.toString(), createCompanyBinding.cityText.text.toString(),
                createCompanyBinding.streetText.text.toString(), createCompanyBinding.houseText.text.toString())

            lifecycleScope.launch(Dispatchers.IO) {
                Messages().showSuccess("Компания добавлена", view)
                CompanyRepository().add(company)

                delay(600)

                val intent = Intent(this@CreateCompanyActivity, CompaniesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

                startActivity(intent)

            }

        }
        else{
            Toast.makeText(this, "Не все поля корректно заполнены", Toast.LENGTH_LONG).show()
        }
    }
}