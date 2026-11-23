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

sealed interface ScanHistoryState {
    object Loading : ScanHistoryState
    data class Success(val scans: List<Scan>) : ScanHistoryState
    data class Error(val message: String) : ScanHistoryState
}

class ScanHistoryViewModel(
    private val scanRepository: ScanRepository = ScanRepository()
) : ViewModel() {

    private val _historyState = MutableStateFlow<ScanHistoryState>(ScanHistoryState.Loading)
    val historyState: StateFlow<ScanHistoryState> = _historyState.asStateFlow()

    init {
        loadScans()
    }

    fun loadScans(filterStatus: String? = null, searchQuery: String? = null) {
        _historyState.value = ScanHistoryState.Loading
        viewModelScope.launch {
            val isHealthy = when (filterStatus) {
                "Healthy" -> true
                "Diseased" -> false
                else -> null
            }
            val search = searchQuery?.takeIf { it.isNotBlank() }

            val result = scanRepository.getScans(isHealthy = isHealthy, search = search)
            _historyState.value = when (result) {
                is NetworkResult.Success -> ScanHistoryState.Success(result.data.results)
                is NetworkResult.Error -> ScanHistoryState.Error(result.message)
                is NetworkResult.Exception -> ScanHistoryState.Error(
                    result.throwable.localizedMessage ?: "Failed to load scan history."
                )
            }
        }
    }

    fun deleteScan(id: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            val result = scanRepository.deleteScan(id)
            if (result is NetworkResult.Success) {
                loadScans()
                onComplete()
            }
        }
    }
}
