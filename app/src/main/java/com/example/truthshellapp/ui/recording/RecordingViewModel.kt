package com.example.truthshellapp.ui.recording

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecordingViewModel : ViewModel() {

    // LiveData to hold the recognized text
    private val _recognizedText = MutableLiveData<String?>()
    val recognizedText: LiveData<String?> = _recognizedText

    // LiveData for state management (e.g., IDLE, LISTENING, PROCESSING, ERROR)
    private val _recordingState = MutableLiveData<RecordingState>()
    val recordingState: LiveData<RecordingState> = _recordingState

    init {
        _recordingState.value = RecordingState.IDLE
    }

    fun setRecognizedText(text: String) {
        _recognizedText.value = text
    }

    fun updateState(newState: RecordingState) {
        _recordingState.value = newState
    }

    fun clearRecognizedText() {
        _recognizedText.value = null
    }

    // Optional: Add functions to interact with SpeechRecognizer if logic is moved here
}

enum class RecordingState {
    IDLE, // Initial state or after completion/error
    REQUESTING_PERMISSION,
    READY_TO_LISTEN,
    LISTENING,
    PROCESSING, // After speech ends, before results
    ERROR
}

