package com.ludwig.flowpay.data.local

import android.content.Context
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplate
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.aead.PredefinedAeadParameters
import com.google.crypto.tink.integration.android.AndroidKeysetManager


const val USER_PROFILE_FILE = "user_profile.pb"
private const val KEYSET_NAME = "user_profile_keyset"
private const val KEYSET_PREFS = "user_profile_keyset_prefs"
private const val MASTER_KEY_URI = "android-keystore://user_profile_master_key"

object TinkAeadProvider {

    fun getAead(context: Context): Aead {
        val appContext = context.applicationContext

        val keysetHandle = AndroidKeysetManager.Builder()
            .withSharedPref(appContext, KEYSET_NAME, KEYSET_PREFS)
            .withKeyTemplate(KeyTemplate.createFrom(PredefinedAeadParameters.AES256_GCM))
            .withMasterKeyUri(MASTER_KEY_URI)
            .build()
            .keysetHandle
        
        return keysetHandle.getPrimitive(
            RegistryConfiguration.get(),
            Aead::class.java
        )
    }
}