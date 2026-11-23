package com.example.mavuno.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mavuno.data.model.Scan
import com.example.mavuno.data.remote.NetworkResult
import com.example.mavuno.data.repository.ScanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ScanDetailState {
    object Loading : ScanDetailState
    data class Success(val scan: Scan) : ScanDetailState
    data class Error(val message: String) : ScanDetailState
}

class ScanDetailViewModel(
    private val scanRepository: ScanRepository = ScanRepository()
) : ViewModel() {

    private val _detailState = MutableStateFlow<ScanDetailState>(ScanDetailState.Loading)
    val detailState: StateFlow<ScanDetailState> = _detailState.asStateFlow()

    fun loadScanDetail(id: String) {
        _detailState.value = ScanDetailState.Loading
        viewModelScope.launch {
            val result = scanRepository.getScanDetail(id)
            _detailState.value = when (result) {
                is NetworkResult.Success -> ScanDetailState.Success(result.data)
                is NetworkResult.Error -> ScanDetailState.Error(result.message)
                is NetworkResult.Exception -> ScanDetailState.Error(
                    result.throwable.localizedMessage ?: "Failed to load scan details."
                )
            }
        }
    }

    fun deleteScan(id: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val result = scanRepository.deleteScan(id)
            if (result is NetworkResult.Success) {
                onSuccess()
            }
        }
    }
}
