package com.example.idzmobile.data

import javax.inject.Inject

class DataRepository @Inject constructor(private val apiInteface: ApiInteface) {
    fun login(username: String, password: String) = apiInteface.login(username, password)
    
    fun register(username: String, password: String) = apiInteface.register(username, password)
    
    fun transfer(token: String?, request: Map<String, Any>) = apiInteface.transfer(token, request)
    
    suspend fun getTransactions(token: String?) = apiInteface.getTransactions(token)
    
    suspend fun getBalance(token: String?) = apiInteface.getBalance(token)
    
    suspend fun getPayees(token: String?) = apiInteface.getPayees(token)
}