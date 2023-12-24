package ru.trading_company.tc_v16.repositories

import com.example.publishinghousekotlin.adapters.LocalDateAdapter
import com.example.publishinghousekotlin.http.responses.MessageResponse
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.trading_company.tc_v16.DTOmodels.ShipmentDTO
import ru.trading_company.tc_v16.MyApplication
import ru.trading_company.tc_v16.R
import ru.trading_company.tc_v16.http.JwtInterceptor
import ru.trading_company.tc_v16.models.Purchase
import ru.trading_company.tc_v16.models.Shipment
import java.time.LocalDate

class ShipmentRepository {
    private val client = OkHttpClient.Builder().addInterceptor(JwtInterceptor()).build()
    private val gson = GsonBuilder().registerTypeAdapter(LocalDate::class.java, LocalDateAdapter()).create()
    private val apiUrl = MyApplication.instance.applicationContext.resources.getString(R.string.server) + "/shipments"

    suspend fun get() : List<Shipment>?{

        val request = Request.Builder()
            .url(apiUrl)
            .build()

        val response = client.newCall(request).execute()

        if(response.isSuccessful){
            val typeOfData = object : TypeToken<List<Shipment>>() {}.type
            return gson.fromJson(response.body?.string(), typeOfData);
        }

        return null
    }

    suspend fun add(shipment: ShipmentDTO) : Int{
        val shipmentAsJson = gson.toJson(shipment)

        val mediaType = "application/json; chapset=utf-8".toMediaType()
        val body = shipmentAsJson.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("$apiUrl/create")
            .post(body)
            .build()

        val response = client.newCall(request).execute()

        return response.code
    }

    suspend fun delete(shipmentId : Int): MessageResponse?{
        val request = Request.Builder()
            .url("$apiUrl/delete/$shipmentId")
            .delete()
            .build()

        val response = client.newCall(request).execute()

        if(response.code == 200){
            return MessageResponse(response.code, response.body!!.string())
        }

        return null
    }

    suspend fun update(shipment: Shipment): Int{
        val shipmentAsJson = gson.toJson(shipment)
        val mediaType = "application/json; chapset=utf-8".toMediaType()
        val body = shipmentAsJson.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("$apiUrl/update")
            .put(body)
            .build()

        val response = client.newCall(request).execute()

        return response.code
    }
}