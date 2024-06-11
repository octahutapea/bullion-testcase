package com.bullion.bulliontestcase.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailResponse(

	@field:SerializedName("data")
	val data: DataDetail,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("iserror")
	val iserror: Boolean,

	@field:SerializedName("status")
	val status: Int
)

data class DataDetail(

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("phone")
	val phone: String,

	@field:SerializedName("date_of_birth")
	val dateOfBirth: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("photo")
	val photo: String,

	@field:SerializedName("_id")
	val id: String,

	@field:SerializedName("email")
	val email: String
)
