package com.bullion.bulliontestcase.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bullion.bulliontestcase.data.local.UserPreferences
import com.bullion.bulliontestcase.data.remote.response.UserItem
import com.bullion.bulliontestcase.data.remote.response.UsersResponse
import com.bullion.bulliontestcase.data.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val userPreferences: UserPreferences) : ViewModel(), KoinComponent {

    private val apiService: ApiService by inject()
    private val token = userPreferences.getToken().asLiveData()

    private val _users = MutableLiveData<List<UserItem>>()
    val users : LiveData<List<UserItem>> = _users

    init{
        token.observeForever { token ->
            token?.let {
                getUsers(it)
            }
        }
    }

    private fun getUsers(token: String) {

        val call = apiService.getUsers("Bearer $token")

        call.enqueue(object : Callback<UsersResponse> {
            override fun onResponse(call: Call<UsersResponse>, response: Response<UsersResponse>) {
                if (response.isSuccessful) {
                    _users.postValue(response.body()?.data)
                    Log.e("Response List", response.body()?.data.toString())
                }
                else {
                    Log.e("Response List", response.message())
                }
            }

            override fun onFailure(call: Call<UsersResponse>, response: Throwable) {
                Log.e("Response List", response.toString())
            }
        })

    }

    fun logout(callback : (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferences.clearToken()

            withContext(Dispatchers.Main) {
                callback(true)
            }
        }
    }

}