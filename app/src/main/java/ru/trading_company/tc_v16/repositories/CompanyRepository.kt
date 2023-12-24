package ru.trading_company.tc_v16.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.publishinghousekotlin.adapters.LocalDateAdapter
import com.example.publishinghousekotlin.http.responses.MessageResponse
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.trading_company.tc_v16.MyApplication
import ru.trading_company.tc_v16.R
import ru.trading_company.tc_v16.http.JwtInterceptor
import ru.trading_company.tc_v16.models.Company
import ru.trading_company.tc_v16.models.Shop
import java.io.File
import java.time.LocalDate

class CompanyRepository {
    private val client = OkHttpClient.Builder().addInterceptor(JwtInterceptor()).build()
    private val gson = GsonBuilder().registerTypeAdapter(LocalDate::class.java, LocalDateAdapter()).create()
    private val apiUrl = MyApplication.instance.applicationContext.resources.getString(R.string.server) + "/companies"

    suspend fun get() : List<Company>?{

        val request = Request.Builder()
            .url(apiUrl)
            .build()

        val response = client.newCall(request).execute()

        if(response.isSuccessful){
            val typeOfData = object : TypeToken<List<Company>>() {}.type
            return gson.fromJson(response.body?.string(), typeOfData);
        }

        return null
    }

    suspend fun getById(id: Int) : Company?{

        val request = Request.Builder()
            .url(apiUrl + "/$id")
            .build()

        val response = client.newCall(request).execute()

        if(response.isSuccessful){
            return gson.fromJson(response.body?.string(), Company::class.java)
        }

        return null
    }

    suspend fun add(company: Company) : Int{
        val companyAsJson = gson.toJson(company)

        val mediaType = "application/json; chapset=utf-8".toMediaType()
        val body = companyAsJson.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("$apiUrl/create")
            .post(body)
            .build()

        val response = client.newCall(request).execute()

        return response.code
    }

    suspend fun delete(companyId : Int): MessageResponse?{
        val request = Request.Builder()
            .url("$apiUrl/delete/$companyId")
            .delete()
            .build()

        val response = client.newCall(request).execute()

        if(response.code == 200){
            return MessageResponse(response.code, response.body!!.string())
        }

        return null
    }

    suspend fun update(company: Company): Int{
        val companyAsJson = gson.toJson(company)
        val mediaType = "application/json; chapset=utf-8".toMediaType()
        val body = companyAsJson.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("$apiUrl/update")
            .put(body)
            .build()

        val response = client.newCall(request).execute()

        return response.code
    }
}