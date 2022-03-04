package com.example.idzmobile.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("accountNo")
	val accountNo: String? = null,
	
	@field:SerializedName("status")
	val status: String? = null,
	
	@field:SerializedName("token")
	val token: String? = null,
	
	@field:SerializedName("username")
	val username: String? = null,
	
	@field:SerializedName("error")
	val error: String? = null
)
