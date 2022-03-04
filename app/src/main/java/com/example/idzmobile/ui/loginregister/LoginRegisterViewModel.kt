package com.example.idzmobile.ui.loginregister

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.idzmobile.data.DataRepository
import javax.inject.Inject

class LoginRegisterViewModel: ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean>
        get() = _isLoading
    
    private val _fragmentNumber = MutableLiveData<Int>()
    val fragmentNumber : LiveData<Int>
        get() = _fragmentNumber
    
    fun setLoading(boolean: Boolean){
        _isLoading.postValue(boolean)
    }
    
    fun changeFragment(fragmentNumber : Int){
        _fragmentNumber.postValue(fragmentNumber)
    }
}