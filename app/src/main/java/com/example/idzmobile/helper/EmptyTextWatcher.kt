package com.example.idzmobile.helper

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputLayout

class EmptyTextWatcher(private val layout: TextInputLayout, private val message: String) : TextWatcher {
    
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    
    }
    
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    
    }
    
    override fun afterTextChanged(p0: Editable?) {
        if(p0?.length == 0){
            layout.error = message
        } else {
            layout.error = null
        }
    }
}