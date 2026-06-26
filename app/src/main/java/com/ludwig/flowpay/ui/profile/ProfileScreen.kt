package com.ludwig.flowpay.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ludwig.flowpay.ui.navigation.Screens
import kotlin.String

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    navToScreen: (Screens) -> Unit
) {

    val profileDetailsData by profileViewModel.profileDetails.collectAsStateWithLifecycle()


    var profileDetailsUIState by remember { mutableStateOf(ProfileDetailsUIState()) }
    LaunchedEffect(profileDetailsData) {
        profileDetailsUIState = profileDetailsData
    }
    val dataHasChanges = profileDetailsUIState != profileDetailsData


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileTextField(
            label = "Name",
            value = profileDetailsUIState.name,
            onValueChange = { profileDetailsUIState = profileDetailsUIState.copy(name = it) },
        )
        ProfileTextField(
            label = "Email",
            value = profileDetailsUIState.email,
            onValueChange = { profileDetailsUIState = profileDetailsUIState.copy(email = it) },
        )
        ProfileTextField(
            label = "Street line 1",
            value = profileDetailsUIState.streetLine1,
            onValueChange = {
                profileDetailsUIState = profileDetailsUIState.copy(streetLine1 = it)
            },
        )
        ProfileTextField(
            label = "Street line 2",
            value = profileDetailsUIState.streetLine2,
            onValueChange = {
                profileDetailsUIState = profileDetailsUIState.copy(streetLine2 = it)
            },
        )
        ProfileTextField(
            label = "City",
            value = profileDetailsUIState.city,
            onValueChange = { profileDetailsUIState = profileDetailsUIState.copy(city = it) },
        )
        ProfileTextField(
            label = "Region",
            value = profileDetailsUIState.region,
            onValueChange = { profileDetailsUIState = profileDetailsUIState.copy(region = it) },
        )
        ProfileTextField(
            label = "Country",
            value = profileDetailsUIState.country,
            onValueChange = { profileDetailsUIState = profileDetailsUIState.copy(country = it) },
        )
        ProfileTextField(
            label = "Postcode",
            value = profileDetailsUIState.postcode,
            onValueChange = { profileDetailsUIState = profileDetailsUIState.copy(postcode = it) },
        )
        if (dataHasChanges) {
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    profileViewModel.saveProfileDetails(
                        name = profileDetailsUIState.name.takeIf { it != profileDetailsData.name },
                        email = profileDetailsUIState.email.takeIf { it != profileDetailsData.email },
                        streetLine1 = profileDetailsUIState.streetLine1.takeIf { it != profileDetailsData.streetLine1 },
                        streetLine2 = profileDetailsUIState.streetLine2.takeIf { it != profileDetailsData.streetLine2 },
                        city = profileDetailsUIState.city.takeIf { it != profileDetailsData.city },
                        region = profileDetailsUIState.region.takeIf { it != profileDetailsData.region },
                        country = profileDetailsUIState.country.takeIf { it != profileDetailsData.country },
                        postcode = profileDetailsUIState.postcode.takeIf { it != profileDetailsData.postcode },
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Save")
            }
        }
    }
}

@Composable
private fun ProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        singleLine = label != "Street line 1" && label != "Street line 2",
    )
}