package com.ludwig.flowpay.data.repository

import androidx.datastore.core.DataStore
import com.ludwig.flowpay.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserProfileRepository(
    private val store: DataStore<UserProfile>
) {

    val profile: Flow<UserProfile> = store.data

    val name: Flow<String> = store.data.map { it.name }
    suspend fun saveName(name: String) {
        store.updateData { current ->
            current.toBuilder()
                .setName(name)
                .build()
        }
    }

    val email: Flow<String> = store.data.map { it.email }
    suspend fun saveEmail(email: String) {
        store.updateData { current ->
            current.toBuilder()
                .setEmail(email)
                .build()
        }
    }

    val streetLine1: Flow<String> = store.data.map { it.streetLine1 }
    suspend fun saveStreetLine1(streetLine1: String) {
        store.updateData { current ->
            current.toBuilder()
                .setStreetLine1(streetLine1)
                .build()
        }
    }

    suspend fun saveStreetLine2(streetLine2: String) {
        store.updateData { current ->
            current.toBuilder()
                .setStreetLine2(streetLine2)
                .build()
        }
    }

    suspend fun saveCity(city: String) {
        store.updateData { current ->
            current.toBuilder()
                .setCity(city)
                .build()
        }
    }

    suspend fun saveRegion(region: String) {
        store.updateData { current ->
            current.toBuilder()
                .setRegion(region)
                .build()
        }
    }

    suspend fun saveCountry(country: String) {
        store.updateData { current ->
            current.toBuilder()
                .setCountry(country)
                .build()
        }
    }

    suspend fun savePostcode(postcode: String) {
        store.updateData { current ->
            current.toBuilder()
                .setPostcode(postcode)
                .build()
        }
    }

}