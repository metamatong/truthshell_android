package com.example.truthshellapp.ui.inprogress

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.truthshellapp.data.model.SonarResponse
import com.example.truthshellapp.data.repository.SonarRepository
import kotlinx.coroutines.launch
import retrofit2.Response

// Define states for the API call process
enum class ApiState {
    LOADING,
    SUCCESS,
    ERROR
}

class InProgressViewModel(private val repository: SonarRepository = SonarRepository()) : ViewModel() {

    private val _apiState = MutableLiveData<ApiState>()
    val apiState: LiveData<ApiState> = _apiState

    private val _sonarResult = MutableLiveData<SonarResponse?>()
    val sonarResult: LiveData<SonarResponse?> = _sonarResult

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun checkClaimWithApi(apiKey: String, claimText: String) {
        _apiState.value = ApiState.LOADING
        viewModelScope.launch {
            try {
                val response = repository.checkClaim(apiKey, claimText)
                if (response.isSuccessful) {
                    _sonarResult.value = response.body()
                    _apiState.value = ApiState.SUCCESS
                } else {
                    // Handle API error response (e.g., 4xx, 5xx)
                    val errorBody = response.errorBody()?.string() ?: "Unknown API error"
                    _errorMessage.value = "API Error (${response.code()}): $errorBody"
                    _apiState.value = ApiState.ERROR
                }
            } catch (e: Exception) {
                // Handle network or other exceptions
                _errorMessage.value = "Network Error: ${e.message}"
                _apiState.value = ApiState.ERROR
            }
        }
    }
}

