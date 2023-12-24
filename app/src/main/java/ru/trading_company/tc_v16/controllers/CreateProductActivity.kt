package ru.trading_company.tc_v16.controllers

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.publishinghousekotlin.basics.FileWorker

import com.example.publishinghousekotlin.basics.Listener
import com.example.publishinghousekotlin.basics.Messages

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.trading_company.tc_v16.DTOmodels.PurchaseDTO
import ru.trading_company.tc_v16.R
import ru.trading_company.tc_v16.adapters.PurchaseListAdapter
import ru.trading_company.tc_v16.databinding.ActivityCreateProductBinding
import ru.trading_company.tc_v16.models.Company
import ru.trading_company.tc_v16.models.Product
import ru.trading_company.tc_v16.repositories.CompanyRepository
import ru.trading_company.tc_v16.repositories.ProductRepository
import ru.trading_company.tc_v16.repositories.PurchaseRepository
import java.io.File

class CreateProductActivity : AppCompatActivity() {

    private lateinit var createProductBinding: ActivityCreateProductBinding
    private var imagePickerLauncher: ActivityResultLauncher<Intent>? = null
    private var selectedImageUri: Uri? = null
    private var selectedBitMapPhoto: Bitmap? = null

    private lateinit var product: Product
    private var companies = arrayListOf<Company>()
    private var companyId = 0
    private lateinit var view: ViewGroup
    private var companyIsSelected: Boolean = false

    companion object{
        val IMAGE_REQUES_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_product)

        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar


        // Иконка назад на тулбаре
        toolbar.setNavigationOnClickListener {
            Log.e("BACKTOWAREHOUSE", "YES")
            finish()
        }

        view = findViewById<View>(android.R.id.content) as ViewGroup
        
        createProductBinding = ActivityCreateProductBinding.inflate(layoutInflater)
        setContentView(createProductBinding.root)

        var companyList = arrayListOf<String>()

        lifecycleScope.launch(Dispatchers.IO) {
            companies = CompanyRepository().get() as ArrayList<Company>

            for (item: Company in companies){
                companyList.add(item.name)
            }

            withContext(Dispatchers.Main){
                createProductBinding.spinnerCompany.adapter = ArrayAdapter(this@CreateProductActivity, android.R.layout.simple_spinner_item, companyList)
            }
        }

        setListeners()

        createProductBinding.spinnerCompany.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                companyIsSelected = true
                companyId = position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        createProductBinding.addPhotoButton.setOnClickListener {
            pickImage()
        }

        createProductBinding.addNewProductBtn.setOnClickListener {
            save()
        }
    }

    private fun pickImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUES_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUES_CODE && resultCode == RESULT_OK){
            selectedImageUri = data?.data
            if (Build.VERSION.SDK_INT >= 28){
                val source = ImageDecoder.createSource(this.contentResolver, selectedImageUri!!)
                selectedBitMapPhoto = ImageDecoder.decodeBitmap(source)
                createProductBinding.productView.setImageBitmap(selectedBitMapPhoto)
            }
            else{
                selectedBitMapPhoto = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImageUri)
                createProductBinding.productView.setImageBitmap(selectedBitMapPhoto)
            }
        }
    }

    fun setListeners(){
        val listener = Listener()

        listener.nameListener(createProductBinding.titleText, createProductBinding.titleContainer)
        listener.descListener(createProductBinding.descText, createProductBinding.descContainer)
        listener.unitListener(createProductBinding.unitText, createProductBinding.unitContainer)
        listener.vatListener(createProductBinding.vatText, createProductBinding.vatContainer)
        listener.priceListener(createProductBinding.priceText, createProductBinding.priceContainer)
        listener.categoryListener(createProductBinding.categoryText, createProductBinding.categoryContainer)

    }

    private fun save(){
        if (createProductBinding.titleContainer.helperText == null && createProductBinding.descContainer.helperText == null
            && createProductBinding.unitContainer.helperText == null && createProductBinding.vatContainer.helperText == null
            && createProductBinding.priceContainer.helperText == null){

            if(!companyIsSelected){
                Toast.makeText(this@CreateProductActivity, "Выберите компанию!", Toast.LENGTH_LONG).show()
                return
            }

            createProductBinding.addNewProductBtn.isEnabled = false
            createProductBinding.addPhotoButton.isEnabled = false

            var photo: File?

            photo = if(selectedImageUri == null){
                null
            }else{
                FileWorker().uriToFile(selectedImageUri!!, applicationContext)
            }

            var product = Product(0, createProductBinding.titleText.text.toString().trim(), Integer.valueOf(createProductBinding.vatText.text.toString().trim()),
                createProductBinding.descText.text.toString().trim(), createProductBinding.unitText.text.toString().trim(),
                createProductBinding.categoryText.text.toString().trim(), Integer.valueOf(createProductBinding.priceText.text.toString().trim()),
                0, companies.get(companyId), "")

            try{
                lifecycleScope.launch(Dispatchers.IO) {
                    Messages().showSuccess("Товар добавлен", view)
                    ProductRepository().add(product, photo)

                    delay(600)

                    val intent = Intent(this@CreateProductActivity, WarehouseActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

                    startActivity(intent)

                }
            }
            catch (e: Exception){
                selectedImageUri == null
                createProductBinding.addNewProductBtn.isEnabled = true

                Toast.makeText(this, "Изображение слишком большое", Toast.LENGTH_LONG).show()
            }


        }
        else{
            Toast.makeText(this, "Не все поля корректно заполнены", Toast.LENGTH_LONG).show()
        }
    }
}