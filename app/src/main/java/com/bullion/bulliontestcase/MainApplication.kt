package com.bullion.bulliontestcase

import android.app.Application
import com.bullion.bulliontestcase.di.dataStoreModule
import com.bullion.bulliontestcase.di.networkModule
import com.bullion.bulliontestcase.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(viewModelModule, networkModule, dataStoreModule)
        }
    }
}