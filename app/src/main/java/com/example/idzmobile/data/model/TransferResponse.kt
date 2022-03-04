package com.example.idzmobile.data.model

import com.google.gson.annotations.SerializedName

data class TransferResponse(

	@field:SerializedName("amount")
	val amount: Double? = null,

	@field:SerializedName("recipientAccount")
	val recipientAccount: String? = null,

	@field:SerializedName("description")
	val description: Any? = null,

	@field:SerializedName("error")
	val error: String? = null,

	@field:SerializedName("transactionId")
	val transactionId: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
