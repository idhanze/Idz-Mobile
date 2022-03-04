package com.example.idzmobile.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    private fun getInterceptor() : OkHttpClient{
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY;
        
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        
        return client
    }
    
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://green-thumb-64168.uc.r.appspot.com/")
            .client(getInterceptor())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    fun getService() = getRetrofit().create(ApiInteface::class.java)
}