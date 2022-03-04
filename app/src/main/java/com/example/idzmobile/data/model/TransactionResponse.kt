package com.example.idzmobile.data.model

import com.google.gson.annotations.SerializedName

data class TransactionResponse(
	
	@field:SerializedName("data")
	val data: List<DataItemTransaction?>? = null,
	
	@field:SerializedName("status")
	val status: String? = null
)

data class Receipient(

	@field:SerializedName("accountHolder")
	val accountHolder: String? = null,

	@field:SerializedName("accountNo")
	val accountNo: String? = null
)

data class DataItemTransaction(

	@field:SerializedName("transactionType")
	val transactionType: String? = null,

	@field:SerializedName("amount")
	val amount: Double? = null,

	@field:SerializedName("receipient")
	val receipient: Receipient? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("transactionDate")
	val transactionDate: String? = null,

	@field:SerializedName("transactionId")
	val transactionId: String? = null
)

