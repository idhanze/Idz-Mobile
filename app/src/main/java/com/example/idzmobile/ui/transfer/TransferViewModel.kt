package com.example.idzmobile.ui.transfer

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.idzmobile.data.DataRepository
import com.example.idzmobile.data.model.PayeesResponse
import com.example.idzmobile.data.model.TransferResponse
import com.example.idzmobile.helper.Const
import com.example.idzmobile.helper.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class TransferViewModel @Inject constructor(private val dataRepository: DataRepository, @ApplicationContext private val context: Context):
    ViewModel()  {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading
    
    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected : LiveData<Boolean>
        get() = _isConnected
    
    private val _payeesResponse = MutableLiveData<PayeesResponse>()
    val payeesResponse : LiveData<PayeesResponse>
        get() = _payeesResponse
    
    private val _transferResponse = MutableLiveData<TransferResponse>()
    val transferResponse : LiveData<TransferResponse>
        get() = _transferResponse
    
    init {
        getPayees()
    }
    
    fun getPayees() {
        _isLoading.value = true
        val sharedPreferences = context.getSharedPreferences(Const.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val token = sharedPreferences.getString(Const.KEY_TOKEN, "")
    
        if(!Util.isNetworkConnected(context)){
            _isLoading.value = false
            _isLoading.value = false
            return
        }
        
        viewModelScope.launch {
            val payResponse = dataRepository.getPayees(token)
            _payeesResponse.value = payResponse.body()
            
            _isLoading.value = false
            
        }
    }
    
    fun transfer(accountNo: String, amount: Double, description: String){
        _isLoading.value = true
        val sharedPreferences = context.getSharedPreferences(Const.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val token = sharedPreferences.getString(Const.KEY_TOKEN, "")
    
        if(!Util.isNetworkConnected(context)){
            _isLoading.value = false
            _isLoading.value = false
            return
        }
        
        val request = HashMap<String, Any>()
        request["receipientAccountNo"] = accountNo
        request["amount"] = amount
        request["description"] = description
        
        dataRepository.transfer(token, request ).enqueue(object: Callback<TransferResponse> {
            override fun onResponse(call: Call<TransferResponse>, response: Response<TransferResponse>) {
                _transferResponse.value = response.body()
                _isLoading.value = false
            }
    
            override fun onFailure(call: Call<TransferResponse>, t: Throwable) {
                Timber.d(t.localizedMessage)
                _isLoading.value = false
            }
    
        })
    }
}