package ru.trading_company.tc_v16.adapters

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.publishinghousekotlin.basics.FileWorker
import ru.trading_company.tc_v16.R
import ru.trading_company.tc_v16.controllers.DetailProductActivity
import ru.trading_company.tc_v16.models.Product
import ru.trading_company.tc_v16.models.Purchase_List

class AddProductPurchaseAdapter (var productList: List<Product>, val context: Context, var callback:(List<Product>) -> Unit, var callback2:(List<Int>) -> Unit) : RecyclerView.Adapter<AddProductPurchaseAdapter.MyViewHolder>() {

    var checkBoxStateArray = SparseBooleanArray()
    var countProductList = arrayListOf<Int>()
    var flag: Boolean = false
    var genericMap = mutableMapOf<Int, String?>()


    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.productItemTitle)
        val text: TextView = view.findViewById(R.id.productItemText)
        val price: TextView = view.findViewById(R.id.productItemPrice)
        val detailBtn: Button = view.findViewById(R.id.detailProductItemBtn)
        val image: ImageView = view.findViewById(R.id.productItemView)
        val check: CheckBox = view.findViewById(R.id.checkProduct)
        val countProduct: EditText = view.findViewById(R.id.selectCountProduct)

        init {

            if (!flag){
                Log.i("Filler", "Заполнение массива")
                for (i in 0..productList.size){
                    countProductList.add(0)

                    genericMap += i to null
                }
                flag = true
            }

            countProduct.addTextChangedListener {

                if (countProduct.getText().toString().trim().isEmpty()){
                    countProductList[adapterPosition] = 0

                    check.isChecked = false
                    genericMap[adapterPosition] = null
                    checkBoxStateArray.put(adapterPosition, false)
                    setLists()
                }
                else{
                    if (countProduct.getText().toString().trim().equals("0")){
                        check.isChecked = false
                        Toast.makeText(context, "Введите кол-во больше 0", Toast.LENGTH_LONG).show()
                        checkBoxStateArray.put(adapterPosition, false)

                    }
                    countProductList[adapterPosition] = Integer.valueOf(countProduct.getText().toString()!!)
                    setLists()
                    genericMap[adapterPosition] = countProductList[adapterPosition].toString()
                }
            }
            
            check.setOnClickListener {


                    if (!checkBoxStateArray.get(adapterPosition, false)) {
//                        Log.e("KAKOGO", countProductList.get(adapterPosition).toString())
                        if(countProductList.get(adapterPosition) == 0){
                            Handler().postDelayed({ check.isChecked = false }, 300)
                            Toast.makeText(context, "Укажите кол-во товара", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            check.isChecked = true
//                            Log.e("ERRROR", countProductList.get(adapterPosition).toString() + " | " + adapterPosition.toString())
                            checkBoxStateArray.put(adapterPosition, true)
                        }
                    } else {
                        check.isChecked = false

                        checkBoxStateArray.put(adapterPosition, false)
                    }
               // Log.e("IN CHEK LIST", countProductList.toString())
                setLists()
//                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.select_product_item, parent, false)
        return MyViewHolder(view)
    }

    fun setLists(){
        callback(getProducts())
        callback2(getCountProducts())
    }

    fun getProducts(): List<Product>{
        var list = arrayListOf<Product>()

        for(i in 0..productList.size - 1){
            if (checkBoxStateArray.get(i)){
                list.add(productList.get(i))
            }
        }

        return list;
    }

    fun getCountProducts(): List<Int>{
        var listCount = arrayListOf<Int>()

        for(i in 0..productList.size - 1){
            if (checkBoxStateArray.get(i)){
                listCount.add(countProductList.get(i))
            }
        }

        return listCount
    }

    override fun getItemCount(): Int {
        return productList.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = productList[position].name
        holder.text.text = productList[position].characteristic
        holder.price.text = "Цена закупки: " + productList[position].price.toString()
        holder.image.setImageBitmap(FileWorker().getBitmap(productList[position].photo))

        holder.countProduct.setText(genericMap.get(position))

        holder.countProduct.addTextChangedListener {

            if (holder.countProduct.getText().toString().trim().isEmpty()){
                holder.check.isChecked = false
            }

        }

        holder.check.isChecked = checkBoxStateArray.get(position, false)

        holder.detailBtn.setOnClickListener {
            val intent = Intent(context, DetailProductActivity::class.java)
            intent.putExtra("productId", productList[position].id)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            context.startActivity(intent)
        }
    }
}