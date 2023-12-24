package ru.trading_company.tc_v16.repositories

import com.example.publishinghousekotlin.adapters.LocalDateAdapter
import com.example.publishinghousekotlin.http.responses.MessageResponse
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.trading_company.tc_v16.MyApplication
import ru.trading_company.tc_v16.R
import ru.trading_company.tc_v16.http.JwtInterceptor
import ru.trading_company.tc_v16.models.Company
import ru.trading_company.tc_v16.models.Purchase_List
import java.time.LocalDate

class Purchase_ListRepository {
    private val client = OkHttpClient.Builder().addInterceptor(JwtInterceptor()).build()
    private val gson = GsonBuilder().registerTypeAdapter(LocalDate::class.java, LocalDateAdapter()).create()
    private val apiUrl = MyApplication.instance.applicationContext.resources.getString(R.string.server) + "/purchaseLists"

    suspend fun get() : List<Purchase_List>?{

        val request = Request.Builder()
            .url(apiUrl)
            .build()

        val response = client.newCall(request).execute()

        if(response.isSuccessful){
            val typeOfData = object : TypeToken<List<Purchase_List>>() {}.type
            return gson.fromJson(response.body?.string(), typeOfData);
        }

        return null
    }

    suspend fun getByPurchase(purchaseId: Int): List<Purchase_List>?{

        val request = Request.Builder()
            .url("$apiUrl/innerPurchase/$purchaseId")
            .build()

        val response = client.newCall(request).execute()

        if(response.isSuccessful){
            val typeOfData = object : TypeToken<List<Purchase_List>>() {}.type
            return gson.fromJson(response.body?.string(), typeOfData);
        }

        return null
    }

    suspend fun add(purchase_list: Purchase_List) : Int{
        val purchase_listAsJson = gson.toJson(purchase_list)

        val mediaType = "application/json; chapset=utf-8".toMediaType()
        val body = purchase_listAsJson.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("$apiUrl/create")
            .post(body)
            .build()

        val response = client.newCall(request).execute()

        return response.code
    }

    suspend fun delete(purchase_listId : Int): MessageResponse?{
        val request = Request.Builder()
            .url("$apiUrl/delete/$purchase_listId")
            .delete()
            .build()

        val response = client.newCall(request).execute()

        if(response.code == 200){
            return MessageResponse(response.code, response.body!!.string())
        }

        return null
    }

    suspend fun update(purchase_list: Purchase_List): Int{
        val purchase_listAsJson = gson.toJson(purchase_list)
        val mediaType = "application/json; chapset=utf-8".toMediaType()
        val body = purchase_listAsJson.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("$apiUrl/update")
            .put(body)
            .build()

        val response = client.newCall(request).execute()

        return response.code
    }
}