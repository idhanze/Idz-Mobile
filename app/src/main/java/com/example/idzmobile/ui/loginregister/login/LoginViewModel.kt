package com.example.idzmobile.ui.loginregister.login

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.idzmobile.data.DataRepository
import com.example.idzmobile.data.model.LoginResponse
import com.example.idzmobile.helper.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class LoginViewModel @Inject constructor(private val dataRepository: DataRepository, private val app: Application) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean>
        get() = _isLoading
    
    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected : LiveData<Boolean>
        get() = _isConnected
    
    private val _response = MutableLiveData<LoginResponse>()
    val response : LiveData<LoginResponse>
        get() = _response
    
    fun login(username: String, password: String){
        _isLoading.value = true
        
        if(!Util.isNetworkConnected(app.baseContext)){
            _isLoading.value = false
            _isLoading.value = false
            return
        }
        
        dataRepository.login(username, password).enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                _response.value = response.body()
            }
    
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Timber.d(t.localizedMessage)
                _isLoading.value = false
            }
    
        })
    }
}