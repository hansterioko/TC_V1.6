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
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.publishinghousekotlin.basics.FileWorker
import ru.trading_company.tc_v16.R
import ru.trading_company.tc_v16.controllers.DetailProductActivity
import ru.trading_company.tc_v16.models.Product

class CreateShipmentAdapter (var productList: List<Product>, val context: Context, var callback:(List<Product>) -> Unit, var callback2:(List<Int>) -> Unit, var callback3:(List<Int>) -> Unit) : RecyclerView.Adapter<CreateShipmentAdapter.MyViewHolder>() {

    var checkBoxStateArray = SparseBooleanArray()
    var countProductList = arrayListOf<Int>()
    var priceProductList = arrayListOf<Int>()
    var flag: Boolean = false
    var genericMap = mutableMapOf<Int, String?>()
    var priceMap = mutableMapOf<Int, String?>()


    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.productItemTitle)
        val text: TextView = view.findViewById(R.id.productItemText)
        val count: TextView = view.findViewById(R.id.productCountText)
        val detailBtn: Button = view.findViewById(R.id.detailProductItemBtn)
        val image: ImageView = view.findViewById(R.id.productItemView)
        val check: CheckBox = view.findViewById(R.id.checkProduct)
        val countProduct: EditText = view.findViewById(R.id.selectCountProduct)
        val priceProduct: EditText = view.findViewById(R.id.selectPriceProduct)

        init {

            if (!flag){
                for (i in 0..productList.size){
                    countProductList.add(0)
                    priceProductList.add(0)

                    genericMap += i to null
                    priceMap += i to null
                }
                flag = true
            }

            priceProduct.addTextChangedListener{

                if (priceProduct.getText().toString().trim().isEmpty()){
                    priceProductList[adapterPosition] = 0

                    check.isChecked = false
                    priceMap[adapterPosition] = null
                    checkBoxStateArray.put(adapterPosition, false)
                    setLists()
                }
                else{
                    if (priceProduct.getText().toString().trim().equals("0")){
                        check.isChecked = false
                        priceMap[adapterPosition] = null
                        Toast.makeText(context, "Введите цену больше 0", Toast.LENGTH_LONG).show()
                        checkBoxStateArray.put(adapterPosition, false)

                    }
                    priceProductList[adapterPosition] = Integer.valueOf(priceProduct.getText().toString()!!)
                    setLists()
                    priceMap[adapterPosition] = priceProductList[adapterPosition].toString()
                }

            }

            countProduct.addTextChangedListener {

                if (countProduct.getText().toString().trim().isEmpty()){
                    countProductList[adapterPosition] = 0

                    check.isChecked = false
                    genericMap[adapterPosition] = null
                    checkBoxStateArray.put(adapterPosition, false)
                    setLists()
                }
                else if(Integer.valueOf(countProduct.getText().toString().trim()) > productList.get(adapterPosition).countOnWarehouse){
                    Toast.makeText(context, "Товара на складе недостаточно", Toast.LENGTH_SHORT).show()
                    countProductList[adapterPosition] = 0

                    check.isChecked = false
                    genericMap[adapterPosition] = null
                    checkBoxStateArray.put(adapterPosition, false)
                    countProduct.setText(countProduct.getText().toString().trim().substring(0, countProduct.getText().toString().trim().length - 1))
                    setLists()
                }
                else{
                    if (countProduct.getText().toString().trim().equals("0")){
                        check.isChecked = false
                        genericMap[adapterPosition] = null
                        Toast.makeText(context, "Введите кол-во больше 0", Toast.LENGTH_SHORT).show()
                        checkBoxStateArray.put(adapterPosition, false)

                    }
                    countProductList[adapterPosition] = Integer.valueOf(countProduct.getText().toString()!!)
                    setLists()
                    genericMap[adapterPosition] = countProductList[adapterPosition].toString()
                }
            }

            check.setOnClickListener {

                if (!checkBoxStateArray.get(adapterPosition, false)) {
                    if(countProductList.get(adapterPosition) == 0){
                        Handler().postDelayed({ check.isChecked = false }, 300)
                        Toast.makeText(context, "Укажите кол-во товара", Toast.LENGTH_SHORT).show()
                    }
                    else if(priceProductList.get(adapterPosition) == 0){
                        Handler().postDelayed({ check.isChecked = false }, 300)
                        Toast.makeText(context, "Укажите цену товара", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        check.isChecked = true
                        checkBoxStateArray.put(adapterPosition, true)
                    }
                } else {
                    check.isChecked = false

                    checkBoxStateArray.put(adapterPosition, false)
                }
                setLists()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.select_shipment_product_item, parent, false)
        return MyViewHolder(view)
    }

    fun setLists(){
        callback(getProducts())
        callback2(getCountProducts())
        callback3(getPriceProducts())
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

    fun getPriceProducts(): List<Int>{
        var listPrice = arrayListOf<Int>()

        for(i in 0..productList.size - 1){
            if (checkBoxStateArray.get(i)){
                listPrice.add(priceProductList.get(i))
            }
        }

        return listPrice
    }

    override fun getItemCount(): Int {
        return productList.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = productList[position].name
        holder.text.text = productList[position].characteristic
        holder.count.text = "Кол-во на складе: " + productList[position].countOnWarehouse.toString()
        holder.image.setImageBitmap(FileWorker().getBitmap(productList[position].photo))

        holder.priceProduct.hint = "Цена закупки " + productList[position].price.toString()

        holder.countProduct.setText(genericMap.get(position))

        holder.countProduct.addTextChangedListener {

            if (holder.countProduct.getText().toString().trim().isEmpty() || holder.countProduct.getText().toString().equals("0")){
                holder.check.isChecked = false
            }

        }

        holder.priceProduct.setText(priceMap.get(position))

        holder.priceProduct.addTextChangedListener {

            if (holder.priceProduct.getText().toString().trim().isEmpty() || holder.priceProduct.getText().toString().equals("0")){
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