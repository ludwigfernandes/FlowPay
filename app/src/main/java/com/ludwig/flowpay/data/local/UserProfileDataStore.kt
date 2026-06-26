package com.ludwig.flowpay.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.ludwig.flowpay.UserProfile

private const val USER_PROFILE_FILE = "user_profile.pb"

val Context.userProfileStore: DataStore<UserProfile> by dataStore(
    fileName = USER_PROFILE_FILE,
    serializer = UserProfileSerializer,
)
