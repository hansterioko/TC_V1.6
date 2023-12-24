package ru.trading_company.tc_v16.repositories

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
import ru.trading_company.tc_v16.models.Product
import java.io.File
import java.time.LocalDate

class ProductRepository {
    private val client = OkHttpClient.Builder().addInterceptor(JwtInterceptor()).build()
    private val gson = GsonBuilder().registerTypeAdapter(LocalDate::class.java, LocalDateAdapter()).create()
    private val apiUrl = MyApplication.instance.applicationContext.resources.getString(R.string.server) + "/products"

    suspend fun get() : List<Product>?{

        val request = Request.Builder()
            .url(apiUrl)
            .build()

        val response = client.newCall(request).execute()

        if(response.isSuccessful){
            val typeOfData = object : TypeToken<List<Product>>() {}.type
            return gson.fromJson(response.body?.string(), typeOfData);
        }

        return null
    }

    suspend fun getOne(id: Int) : Product?{

        val request = Request.Builder()
            .url(apiUrl + "/$id")
            .build()

        val response = client.newCall(request).execute()

        if(response.isSuccessful){
            return gson.fromJson(response.body?.string(), Product::class.java);
        }

        return null
    }

    suspend fun getByCompany(id: Int) : List<Product>?{

        val request = Request.Builder()
            .url("$apiUrl/innerCompany/$id")
            .build()

        val response = client.newCall(request).execute()

        if(response.isSuccessful){
            val typeOfData = object : TypeToken<List<Product>>() {}.type
            return gson.fromJson(response.body?.string(), typeOfData);
        }

        return null
    }

    suspend fun add(product: Product, photo: File?) : Int{
        val productAsJson = gson.toJson(product)

        if (photo == null){
            val body = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("product", null, productAsJson.toRequestBody("application/json; chapset=utf-8".toMediaType()))
                .build()

            val request = Request.Builder()
                .url("$apiUrl/create")
                .post(body)
                .build()

            val response = client.newCall(request).execute()

            return response.code
        }
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("product", null, productAsJson.toRequestBody("application/json; chapset=utf-8".toMediaType()))
            .addFormDataPart("photo", photo.name, photo.asRequestBody("image/*".toMediaType()))
            .build()

        val request = Request.Builder()
            .url("$apiUrl/create")
            .post(body)
            .build()

        val response = client.newCall(request).execute()

        return response.code
    }

    suspend fun delete(productId : Int): MessageResponse?{
        val request = Request.Builder()
            .url("$apiUrl/delete/$productId")
            .delete()
            .build()

        val response = client.newCall(request).execute()

        if(response.code == 200){
            return MessageResponse(response.code, response.body!!.string())
        }

        return null
    }

    suspend fun update(product: Product): Int{
        val productAsJson = gson.toJson(product)
        val mediaType = "application/json; chapset=utf-8".toMediaType()
        val body = productAsJson.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("$apiUrl/update/${product.id}")
            .put(body)
            .build()

        val response = client.newCall(request).execute()

        return response.code
    }
}