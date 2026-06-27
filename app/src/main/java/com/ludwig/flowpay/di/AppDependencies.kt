package com.ludwig.flowpay.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.google.crypto.tink.aead.AeadConfig
import com.ludwig.flowpay.BuildConfig
import com.ludwig.flowpay.UserProfile
import com.ludwig.flowpay.data.local.EncryptedUserProfileSerializer
import com.ludwig.flowpay.data.local.USER_PROFILE_FILE
import com.ludwig.flowpay.utils.NetworkConnectivityObserver
import com.ludwig.flowpay.data.remote.CustomWorkerApiService
import com.ludwig.flowpay.data.remote.NetworkModule
import com.ludwig.flowpay.data.repository.CoinRepository
import com.ludwig.flowpay.data.repository.RevolutRepository
import com.ludwig.flowpay.data.repository.UserProfileRepository
import com.revolut.payments.RevolutPaymentsSDK

class AppDependencies(context: Context) {

    init {
        RevolutPaymentsSDK.configure(
            configuration = RevolutPaymentsSDK.Configuration(
                merchantPublicKey = BuildConfig.REVOLUT_PUBLIC_KEY,
                environment = RevolutPaymentsSDK.Environment.SANDBOX
            )
        )

        AeadConfig.register()
    }


    private val customWorkerApiService: CustomWorkerApiService = NetworkModule.customWorkerApiService
    val revolutRepository = RevolutRepository(customWorkerApiService)


    private val coinApiService = NetworkModule.coinApiServer
    val coinRepository = CoinRepository(coinApiService)


    val networkConnectivityObserver = NetworkConnectivityObserver(context)


    val userProfileStore: DataStore<UserProfile> = DataStoreFactory.create(
        serializer = EncryptedUserProfileSerializer(context),
        produceFile = { context.dataStoreFile(USER_PROFILE_FILE) }
    )
    val userProfileRepository: UserProfileRepository = UserProfileRepository(userProfileStore)

}