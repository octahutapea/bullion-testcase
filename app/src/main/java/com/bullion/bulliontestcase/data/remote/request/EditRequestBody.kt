package com.bullion.bulliontestcase.data.remote.request

data class EditRequestBody(
    val firstName: String,
    val lastName: String,
    val gender: String,
    val dateOfBirth: String,
    val email: String,
    val phone: String,
    val address: String
)
