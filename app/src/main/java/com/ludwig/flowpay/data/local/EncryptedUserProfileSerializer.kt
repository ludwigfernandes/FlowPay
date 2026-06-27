package com.ludwig.flowpay.data.local

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.crypto.tink.Aead
import com.google.protobuf.InvalidProtocolBufferException
import com.ludwig.flowpay.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

class EncryptedUserProfileSerializer(context: Context): Serializer<UserProfile> {

    private val aead: Aead = TinkAeadProvider.getAead(context)
    private val associatedData: ByteArray = USER_PROFILE_FILE.encodeToByteArray()

    override val defaultValue: UserProfile = UserProfile.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserProfile {
        return try {
            val encrypted = input.readBytes()
            if (encrypted.isEmpty()) return defaultValue

            val plainBytes = aead.decrypt(encrypted, associatedData)
            UserProfile.parseFrom(plainBytes)

        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read UserProfile", e)
        } catch (e: Exception) {
            throw CorruptionException("Cannot decrypt UserProfile", e)
        }
    }

    override suspend fun writeTo(t: UserProfile, output: OutputStream) {
        val plainBytes = t.toByteArray()
        val encrypted = aead.encrypt(plainBytes, associatedData)
        withContext(Dispatchers.IO) {
            output.write(encrypted)
        }
    }
}