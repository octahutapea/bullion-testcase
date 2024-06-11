package com.bullion.bulliontestcase.di

import android.content.Context
import com.bullion.bulliontestcase.data.local.UserPreferences
import com.bullion.bulliontestcase.data.remote.retrofit.ApiService
import com.bullion.bulliontestcase.ui.edit.EditViewModel
import com.bullion.bulliontestcase.ui.login.LoginViewModel
import com.bullion.bulliontestcase.ui.main.MainViewModel
import com.bullion.bulliontestcase.ui.register.RegisterViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel() }
    viewModel { MainViewModel(get()) }
    viewModel { EditViewModel(get()) }
}

val networkModule = module {
    single {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    single {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://api-test.bullionecosystem.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get<OkHttpClient>())
            .build()
    }

    single {
        get<Retrofit>().create(ApiService::class.java)
    }
}

val dataStoreModule = module {
    single { UserPreferences(get()) }
}
