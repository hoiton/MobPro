package com.example.mydemo.ui.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mydemo.business.user.UserRepositoryPreferences

@Composable
private fun getUserViewModel(): UserViewModel {
    val userRepository = UserRepositoryPreferences(LocalContext.current)
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(userRepository)
    )
    return userViewModel
}

@Composable
fun UserView(viewModel: UserViewModel = getUserViewModel()) {
    Column (
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val user by viewModel.user.collectAsState()
        var username by remember(user.name) { mutableStateOf(user.name) }
        var age by remember(user.age) { mutableIntStateOf(user.age) }
        var authorize by remember(user.authorized) { mutableStateOf(user.authorized) }

        Text(
            style = MaterialTheme.typography.headlineSmall,
            text = "Welcome to the User-Screen"
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = username,
            onValueChange = { value ->
                username = value
            },
            label = { Text("Name") }
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = age.toString(),
            onValueChange = { value ->
                age = value.toIntOrNull() ?: 0
            },
            label = { Text("Age") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Row {
            Switch(
                checked = authorize,
                onCheckedChange = { value ->
                    authorize = value
                },
            )
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 10.dp),
                text = "Authorize user"
            )
        }
        Button(
            onClick = {
                viewModel.updateUser(user.copy(name = username, age = age, authorized = authorize))
            }
        ) {
            Text("Save")
        }
    }
}