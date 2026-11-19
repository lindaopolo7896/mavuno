package com.example.mavuno.ui.screens.dashboard

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mavuno.data.model.Scan
import com.example.mavuno.data.remote.NetworkResult
import com.example.mavuno.data.repository.ScanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ScanState {
    object Idle : ScanState
    object Loading : ScanState
    data class Success(val scan: Scan) : ScanState
    data class Error(val message: String) : ScanState
}

class ScanViewModel(
    private val scanRepository: ScanRepository = ScanRepository()
) : ViewModel() {

    private val _scanState = MutableStateFlow<ScanState>(ScanState.Idle)
    val scanState: StateFlow<ScanState> = _scanState.asStateFlow()

    fun predictScan(context: Context, imageUri: Uri) {
        if (_scanState.value is ScanState.Loading) return

        _scanState.value = ScanState.Loading
        viewModelScope.launch {
            val result = scanRepository.predictScan(context, imageUri)
            _scanState.value = when (result) {
                is NetworkResult.Success -> ScanState.Success(result.data)
                is NetworkResult.Error -> ScanState.Error(result.message)
                is NetworkResult.Exception -> ScanState.Error(
                    result.throwable.localizedMessage ?: "Network error. Please check your connection."
                )
            }
        }
    }

    fun resetState() {
        _scanState.value = ScanState.Idle
    }
}
