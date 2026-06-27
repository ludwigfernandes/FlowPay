package com.ludwig.flowpay

import android.app.Application
import com.ludwig.flowpay.di.AppDependencies

class FlowPayApplication: Application() {

    lateinit var appDependencies: AppDependencies

    override fun onCreate() {
        super.onCreate()

        appDependencies = AppDependencies(applicationContext)

    }
}