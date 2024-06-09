package com.bullion.bulliontestcase.di

import com.bullion.bulliontestcase.ui.login.LoginViewModel
import com.bullion.bulliontestcase.ui.main.MainViewModel
import com.bullion.bulliontestcase.ui.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel() }
    viewModel { RegisterViewModel() }
    viewModel { MainViewModel() }
}