package com.bullion.bulliontestcase.ui.edit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bullion.bulliontestcase.data.local.UserPreferences
import com.bullion.bulliontestcase.data.remote.request.EditRequestBody
import com.bullion.bulliontestcase.data.remote.response.EditResponse
import com.bullion.bulliontestcase.data.remote.response.RegisterResponse
import com.bullion.bulliontestcase.data.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditViewModel(private val userPreferences: UserPreferences) : ViewModel(), KoinComponent {
    private val apiService: ApiService by inject()

    private val token = userPreferences.getToken().asLiveData().value.toString()

    fun postEdit(id: String, editRequestBody: EditRequestBody, callback: (Boolean) -> Unit) {

        val call = apiService.postEdit("Bearer $token", id, editRequestBody)

        call.enqueue(object : Callback<EditResponse> {
            override fun onResponse(call: Call<EditResponse>, response: Response<EditResponse>) {
                if (response.isSuccessful) {
                    callback(true)
                    Log.e("Response Edit", response.body().toString())
                }
                else {
                    callback(false)
                    Log.e("Response Edit", response.message())
                }
            }

            override fun onFailure(call: Call<EditResponse>, response: Throwable) {
                callback(false)
                Log.e("Response Edit", response.toString())
            }
        })
    }

}