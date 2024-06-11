package com.bullion.bulliontestcase.ui.register

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.bullion.bulliontestcase.data.remote.response.RegisterResponse
import com.bullion.bulliontestcase.data.remote.retrofit.ApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class RegisterViewModel : ViewModel(), KoinComponent {
    private val apiService: ApiService by inject()

    fun postRegister(
        firstName: String,
        lastName: String,
        gender: String,
        dateOfBirth: String,
        email: String,
        phoneNumber: String,
        address: String,
        photoProfile: Uri,
        password: String,
        context: Context,
        callback: (Boolean) -> Unit)
    {
        val firstNameBody = firstName.toRequestBody("text/plain".toMediaTypeOrNull())
        val lastNameBody = lastName.toRequestBody("text/plain".toMediaTypeOrNull())
        val genderBody = gender.toRequestBody("text/plain".toMediaTypeOrNull())
        val dateOfBirthBody = dateOfBirth.toRequestBody("text/plain".toMediaTypeOrNull())
        val emailBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val phoneNumberBody = phoneNumber.toRequestBody("text/plain".toMediaTypeOrNull())
        val addressBody = address.toRequestBody("text/plain".toMediaTypeOrNull())
        val passwordBody = password.toRequestBody("text/plain".toMediaTypeOrNull())

        val photo = uriToFile(photoProfile, context)

        val requestImageFile = photo.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            photo.name,
            requestImageFile
        )

        val call = apiService.postRegister(
            firstNameBody,
            lastNameBody,
            genderBody,
            dateOfBirthBody,
            emailBody,
            phoneNumberBody,
            addressBody,
            passwordBody,
            multipartBody
        )

        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    callback(true)
                    Log.e("Response Register", response.body().toString())
                }
                else {
                    callback(false)
                    Log.e("Response Register", response.message())
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, response: Throwable) {
                callback(false)
                Log.e("Response Register", response.toString())
            }
        })
    }

    private fun uriToFile(photoProfile: Uri, context: Context): File {
        val myFile = createCustomTempFile(context)
        val inputStream = context.contentResolver.openInputStream(photoProfile) as InputStream
        val outputStream = FileOutputStream(myFile)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
        outputStream.close()
        inputStream.close()
        return myFile
    }

    private fun createCustomTempFile(context: Context): File {
        val filesDir = context.externalCacheDir
        val timeStamp: String = System.currentTimeMillis().toString()
        return File.createTempFile(timeStamp, ".jpg", filesDir)
    }

}