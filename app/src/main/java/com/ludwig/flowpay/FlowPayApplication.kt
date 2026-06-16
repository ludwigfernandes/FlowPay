package com.ludwig.flowpay

import android.app.Application
import com.ludwig.flowpay.di.AppDependencies
import com.revolut.payments.RevolutPaymentsSDK

class FlowPayApplication: Application() {

    lateinit var appDependencies: AppDependencies

    override fun onCreate() {
        super.onCreate()

        appDependencies = AppDependencies()


        RevolutPaymentsSDK.configure(
            configuration = RevolutPaymentsSDK.Configuration(
                merchantPublicKey = BuildConfig.REVOLUT_PUBLIC_KEY,
                environment = RevolutPaymentsSDK.Environment.SANDBOX
            )
        )
    }
}