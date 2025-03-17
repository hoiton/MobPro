package com.example.mydemo.ui.user

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mydemo.business.user.User
import com.example.mydemo.business.user.UserRepositoryPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModelFactory(
    private val userRepository: UserRepositoryPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class UserViewModel(
    private val userRepository: UserRepositoryPreferences
) : ViewModel() {
    val user: StateFlow<User> = userRepository
        .user
        .stateIn(viewModelScope, SharingStarted.Lazily, User("", -1, false))

    fun updateUser(user: User) {
        viewModelScope.launch {
            userRepository.setUserName(user.name)
            userRepository.setUserAge(user.age)
            userRepository.setUserAuthorize(user.authorized)
        }
    }
}