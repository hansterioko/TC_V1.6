package ru.trading_company.tc_v16.repositories

import android.util.Log
import com.example.publishinghousekotlin.adapters.LocalDateAdapter
import com.example.publishinghousekotlin.http.responses.MessageResponse
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.trading_company.tc_v16.DTOmodels.PurchaseDTO
import ru.trading_company.tc_v16.MyApplication
import ru.trading_company.tc_v16.R
import ru.trading_company.tc_v16.http.JwtInterceptor
import ru.trading_company.tc_v16.models.Company
import ru.trading_company.tc_v16.models.Purchase
import java.time.LocalDate

class PurchaseRepository {
    private val client = OkHttpClient.Builder().addInterceptor(JwtInterceptor()).build()
    private val gson = GsonBuilder().registerTypeAdapter(LocalDate::class.java, LocalDateAdapter()).create()
    private val apiUrl = MyApplication.instance.applicationContext.resources.getString(R.string.server) + "/purchases"

    suspend fun get() : List<Purchase>?{

        val request = Request.Builder()
            .url(apiUrl)
            .build()

        val response = client.newCall(request).execute()

        if(response.isSuccessful){
            val typeOfData = object : TypeToken<List<Purchase>>() {}.type
            return gson.fromJson(response.body?.string(), typeOfData);
        }

        return null
    }

    suspend fun add(purchase: PurchaseDTO) : Int{
        val purchaseAsJson = gson.toJson(purchase)

        val mediaType = "application/json; chapset=utf-8".toMediaType()
        val body = purchaseAsJson.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("$apiUrl/create")
            .post(body)
            .build()

//        Log.e("LISTPR", purchase.purchase_list.size.toString())

        val response = client.newCall(request).execute()

        return response.code
    }

    suspend fun delete(purchaseId : Int): MessageResponse?{
        val request = Request.Builder()
            .url("$apiUrl/delete/$purchaseId")
            .delete()
            .build()

        val response = client.newCall(request).execute()

        if(response.code == 200){
            return MessageResponse(response.code, response.body!!.string())
        }

        return null
    }

    suspend fun update(purchase: Purchase): Int{
        val purchaseAsJson = gson.toJson(purchase)
        val mediaType = "application/json; chapset=utf-8".toMediaType()
        val body = purchaseAsJson.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("$apiUrl/update")
            .put(body)
            .build()

        val response = client.newCall(request).execute()

        return response.code
    }
}