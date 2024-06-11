package com.bullion.bulliontestcase.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bullion.bulliontestcase.data.local.UserPreferences
import com.bullion.bulliontestcase.data.remote.request.LoginRequestBody
import com.bullion.bulliontestcase.data.remote.response.LoginResponse
import com.bullion.bulliontestcase.data.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val userPreferences: UserPreferences) : ViewModel(), KoinComponent {
    private val apiService: ApiService by inject()

    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin: LiveData<Boolean> get() = _isLogin

    init {
        checkLogin()
    }

    private fun checkLogin() {
        viewModelScope.launch {
            val isLoggedIn = userPreferences.getIsLogin().first()
            _isLogin.postValue(isLoggedIn)
        }
    }

    fun postLogin(loginRequestBody: LoginRequestBody, callback: (Boolean) -> Unit) {
        val call = apiService.postLogin(loginRequestBody)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    callback(true)
                    viewModelScope.launch(Dispatchers.IO) {
                        response.body()?.data?.token?.let { userPreferences.saveToken(it) }
                    }
                    Log.e("Response Login", response.body().toString())
                }
                else {
                    callback(false)
                    Log.e("Response Login", response.message())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, response: Throwable) {
                callback(false)
                Log.e("Response Login", response.toString())
            }
        })
    }
}