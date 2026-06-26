package com.ludwig.flowpay.data.repository

import androidx.datastore.core.DataStore
import com.ludwig.flowpay.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserProfileRepository(
    private val store: DataStore<UserProfile>
) {

    val profileDetails: Flow<UserProfile> = store.data
    suspend fun saveProfileDetails(
        name: String?,
        email: String?,
        streetLine1: String?,
        streetLine2: String?,
        city: String?,
        region: String?,
        country: String?,
        postcode: String?,
    ) {
        store.updateData { current ->
            val builder = current.toBuilder()
            name?.let { builder.setName(it) }
            email?.let { builder.setEmail(it) }
            streetLine1?.let { builder.setStreetLine1(it) }
            streetLine2?.let { builder.setStreetLine2(it) }
            city?.let { builder.setCity(it) }
            region?.let { builder.setRegion(it) }
            country?.let { builder.setCountry(it) }
            postcode?.let { builder.setPostcode(it) }
            builder.build()
        }
    }

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

    val streetLine2: Flow<String> = store.data.map { it.streetLine2 }
    suspend fun saveStreetLine2(streetLine2: String) {
        store.updateData { current ->
            current.toBuilder()
                .setStreetLine2(streetLine2)
                .build()
        }
    }

    val city: Flow<String> = store.data.map { it.city }
    suspend fun saveCity(city: String) {
        store.updateData { current ->
            current.toBuilder()
                .setCity(city)
                .build()
        }
    }

    val region: Flow<String> = store.data.map { it.region }
    suspend fun saveRegion(region: String) {
        store.updateData { current ->
            current.toBuilder()
                .setRegion(region)
                .build()
        }
    }

    val country: Flow<String> = store.data.map { it.country }
    suspend fun saveCountry(country: String) {
        store.updateData { current ->
            current.toBuilder()
                .setCountry(country)
                .build()
        }
    }

    val postcode: Flow<String> = store.data.map { it.postcode }
    suspend fun savePostcode(postcode: String) {
        store.updateData { current ->
            current.toBuilder()
                .setPostcode(postcode)
                .build()
        }
    }

}