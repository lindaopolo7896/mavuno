package com.example.mavuno.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mavuno.data.model.User
import com.example.mavuno.data.remote.NetworkResult
import com.example.mavuno.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ProfileState {
    object Loading : ProfileState
    data class Success(val user: User) : ProfileState
    data class Error(val message: String) : ProfileState
}

sealed interface AccountDeletionState {
    object Idle : AccountDeletionState
    object Loading : AccountDeletionState
    object Success : AccountDeletionState
    data class Error(val message: String) : AccountDeletionState
}

class ProfileViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    private val _updateState = MutableStateFlow<NetworkResult<User>?>(null)
    val updateState: StateFlow<NetworkResult<User>?> = _updateState.asStateFlow()

    private val _deletionState = MutableStateFlow<AccountDeletionState>(AccountDeletionState.Idle)
    val deletionState: StateFlow<AccountDeletionState> = _deletionState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        _profileState.value = ProfileState.Loading
        viewModelScope.launch {
            val result = authRepository.getProfile()
            _profileState.value = when (result) {
                is NetworkResult.Success -> ProfileState.Success(result.data)
                is NetworkResult.Error -> ProfileState.Error(result.message)
                is NetworkResult.Exception -> ProfileState.Error(
                    result.throwable.localizedMessage ?: "Failed to load profile."
                )
            }
        }
    }

    fun updateProfile(fullName: String, phoneNumber: String?) {
        viewModelScope.launch {
            val result = authRepository.updateProfile(fullName, phoneNumber)
            _updateState.value = result
            if (result is NetworkResult.Success) {
                _profileState.value = ProfileState.Success(result.data)
            }
        }
    }

    fun deleteAccount(onSuccess: () -> Unit) {
        _deletionState.value = AccountDeletionState.Loading
        viewModelScope.launch {
            val result = authRepository.deleteAccount()
            _deletionState.value = when (result) {
                is NetworkResult.Success -> {
                    onSuccess()
                    AccountDeletionState.Success
                }
                is NetworkResult.Error -> AccountDeletionState.Error(result.message)
                is NetworkResult.Exception -> AccountDeletionState.Error(
                    result.throwable.localizedMessage ?: "Failed to delete account."
                )
            }
        }
    }

    fun resetUpdateState() {
        _updateState.value = null
    }

    fun resetDeletionState() {
        _deletionState.value = AccountDeletionState.Idle
    }
}
