package com.example.idzmobile.data.model

import com.google.gson.annotations.SerializedName

data class PayeesResponse(
	
	@field:SerializedName("data")
	val payees: List<Payees?>? = null,
	
	@field:SerializedName("status")
	val status: String? = null
)

data class Payees(

	@field:SerializedName("accountNo")
	val accountNo: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)
