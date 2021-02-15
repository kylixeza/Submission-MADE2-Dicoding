package com.kylix.submissionmade2

import android.app.Application
import com.kylix.core.di.databaseModule
import com.kylix.core.di.networkModule
import com.kylix.core.di.repositoryModule
import com.kylix.submissionmade2.di.useCaseModule
import com.kylix.submissionmade2.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}