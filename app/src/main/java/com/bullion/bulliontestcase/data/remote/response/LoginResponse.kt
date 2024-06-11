package com.bullion.bulliontestcase.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val data: LoginData,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("iserror")
	val iserror: Boolean,

	@field:SerializedName("status")
	val status: Int
)

data class LoginData(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("token")
	val token: String
)
