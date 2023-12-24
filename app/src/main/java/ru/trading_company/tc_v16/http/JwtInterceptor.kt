package ru.trading_company.tc_v16.http

import com.example.publishinghousekotlin.http.responses.JwtResponse
import okhttp3.Interceptor
import okhttp3.Response
import ru.trading_company.tc_v16.MyApplication

class JwtInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val headers = request.headers.newBuilder()

        val applicationContext = MyApplication.instance.applicationContext
        //val token = JwtResponse.getFromMemory(applicationContext, applicationContext.resources.getString(R.string.keyForJwtResponse))?.token

//        if(token != null){
//            headers.add("Authorization", "Bearer $token")
//        }

        val newRequest = request.newBuilder()
            .headers(headers.build())
            .build()

        return chain.proceed(newRequest)
    }
}