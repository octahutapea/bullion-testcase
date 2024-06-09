package com.bullion.bulliontestcase

import android.app.Application
import com.bullion.bulliontestcase.di.viewModelModule
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(viewModelModule)
        }
    }
}