package com.example.mydemo.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydemo.business.user.User
import com.example.mydemo.business.user.UserRepositoryPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
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