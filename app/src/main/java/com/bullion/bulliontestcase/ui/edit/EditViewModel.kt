package com.bullion.bulliontestcase.ui.edit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bullion.bulliontestcase.data.local.UserPreferences
import com.bullion.bulliontestcase.data.remote.request.EditRequestBody
import com.bullion.bulliontestcase.data.remote.response.DataDetail
import com.bullion.bulliontestcase.data.remote.response.DetailResponse
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

    private val token = userPreferences.getToken().asLiveData()

    private val _detail = MutableLiveData<DataDetail>()
    val detail : LiveData<DataDetail> = _detail

    fun getDetail(id: String) {

        token.observeForever { token ->
            if (token != null) {
                val call = apiService.getDetail("Bearer $token", id)

                call.enqueue(object : Callback<DetailResponse> {
                    override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>) {
                        if (response.isSuccessful) {
                            _detail.value = response.body()?.data

                            Log.e("Response Detail 1", response.body().toString())
                        } else {
                            Log.e("Response Detail 2", response.message())
                        }
                    }

                    override fun onFailure(call: Call<DetailResponse>, response: Throwable) {
                        Log.e("Response Detail 3", response.toString())
                    }
                })
            } else {
                Log.e("Token", "Token is null")
            }
        }
    }

    fun postEdit(id: String, editRequestBody: EditRequestBody, callback: (Boolean) -> Unit) {

        token.observeForever { token ->
            if (token != null) {
                val call = apiService.postEdit("Bearer $token", id, editRequestBody)

                call.enqueue(object : Callback<EditResponse> {
                    override fun onResponse(call: Call<EditResponse>, response: Response<EditResponse>) {
                        if (response.isSuccessful) {
                            callback(true)
                            Log.e("Response Edit 1", response.body().toString())
                        }
                        else {
                            callback(false)
                            Log.e("Response Edit 2", response.toString())
                        }
                    }

                    override fun onFailure(call: Call<EditResponse>, response: Throwable) {
                        callback(false)
                        Log.e("Response Edit 3", response.toString())
                    }
                })
            } else {
                Log.e("Token", "Token is null")
            }
        }
    }

}