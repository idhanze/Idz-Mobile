package com.example.idzmobile.ui.home

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.idzmobile.data.DataRepository
import com.example.idzmobile.data.model.BalanceResponse
import com.example.idzmobile.data.model.TransactionResponse
import com.example.idzmobile.helper.Const
import com.example.idzmobile.helper.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class HomeViewModel @Inject constructor(private val dataRepository: DataRepository, @ApplicationContext private val context: Context):
    ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading
    
    private val _transactionResponse = MutableLiveData<TransactionResponse>()
    val transactionResponse: LiveData<TransactionResponse>
        get() = _transactionResponse
    
    private val _balanceResponse = MutableLiveData<BalanceResponse>()
    val balanceResponse: LiveData<BalanceResponse>
        get() = _balanceResponse
    
    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected : LiveData<Boolean>
        get() = _isConnected
    
    init {
       getData()
    }
    
    fun getData(){
        _isLoading.value = true
        val sharedPreferences = context.getSharedPreferences(Const.SHARED_PREF_NAME, MODE_PRIVATE)
        val token = sharedPreferences.getString(Const.KEY_TOKEN, "")
    
        if(!Util.isNetworkConnected(context)){
            _isLoading.value = false
            _isLoading.value = false
            return
        }
    
        viewModelScope.launch {
            val transResponse = dataRepository.getTransactions(token)
            val balResponse = dataRepository.getBalance(token)
        
            _transactionResponse.value = transResponse.body()
            _balanceResponse.value = balResponse.body()
        
            _isLoading.value = false
        }
    }
}