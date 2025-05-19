package com.example.truthshellapp.ui.inprogress

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.truthshellapp.data.model.TruthResponse
import com.example.truthshellapp.data.repository.TruthRepository
import kotlinx.coroutines.launch

// Define states for the API call process
enum class ApiState {
    LOADING,
    SUCCESS,
    ERROR
}

class InProgressViewModel(private val repository: TruthRepository = TruthRepository()) : ViewModel() {

    private val _apiState = MutableLiveData<ApiState>()
    val apiState: LiveData<ApiState> = _apiState

    private val _sonarResult = MutableLiveData<TruthResponse?>()
    val sonarResult: LiveData<TruthResponse?> = _sonarResult

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    /** Call the JSON /analyze endpoint */
    fun analyzeTextWithApi(apiKey: String, text: String) {
        _apiState.value = ApiState.LOADING
        viewModelScope.launch {
            try {
                val response = repository.analyzeText(apiKey, text)
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

