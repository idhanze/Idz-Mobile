package com.example.idzmobile.data

import com.example.idzmobile.data.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

@JvmSuppressWildcards
interface ApiInteface {
    
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>
    
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<RegisterResponse>
    
    @GET("transactions")
    suspend fun getTransactions(
        @Header("Authorization") token: String?
    ): Response<TransactionResponse>
    
    @GET("balance")
    suspend fun getBalance(
        @Header("Authorization") token: String?
    ): Response<BalanceResponse>
    
    @GET("payees")
    suspend fun getPayees(
        @Header("Authorization") token: String?
    ): Response<PayeesResponse>
    
    @POST("transfer")
    fun transfer(
        @Header("Authorization") token: String?,
        @Body request: Map<String, Any>
    ): Call<TransferResponse>
    
    
}