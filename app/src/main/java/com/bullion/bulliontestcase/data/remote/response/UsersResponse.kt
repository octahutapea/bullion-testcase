package com.bullion.bulliontestcase.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class UsersResponse(

	@field:SerializedName("data")
	val data: List<UserItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("iserror")
	val iserror: Boolean,

	@field:SerializedName("status")
	val status: Int
)

@Parcelize
data class UserItem(

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
): Parcelable
