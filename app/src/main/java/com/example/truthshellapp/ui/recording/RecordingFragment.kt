package com.example.truthshellapp.ui.recording

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.truthshellapp.R
import com.example.truthshellapp.databinding.FragmentRecordingBinding
import java.util.Locale

class RecordingFragment : Fragment(), RecognitionListener {

    private var _binding: FragmentRecordingBinding? = null
    private val binding get() = _binding!!

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognizerIntent: Intent

    // ViewModel for handling API calls later
    // private val viewModel: RecordingViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startListening()
        } else {
            Toast.makeText(requireContext(), "Permission denied to record audio", Toast.LENGTH_SHORT).show()
            // Handle permission denial (e.g., navigate back or show explanation)
            findNavController().popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordingBinding.inflate(inflater, container, false)

        setupSpeechRecognizer()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonStopRecording.setOnClickListener {
            stopListening()
            // Navigation will happen in onResults or onError
        }

        checkAndRequestPermission()
    }

    private fun setupSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
        speechRecognizer.setRecognitionListener(this)

        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true) // Get partial results if needed
            // putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now") // Optional prompt
        }
    }

    private fun checkAndRequestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                startListening()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO) -> {
                // Show an explanation to the user *asynchronously*
                // For simplicity, just request again
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    private fun startListening() {
        binding.textViewRecordingStatus.text = "Listening..."
        // TODO: Add visual feedback for listening state (e.g., animate shell icon)
        speechRecognizer.startListening(recognizerIntent)
    }

    private fun stopListening() {
        binding.textViewRecordingStatus.text = "Processing..."
        speechRecognizer.stopListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        speechRecognizer.destroy() // Release resources
        _binding = null
    }

    // --- RecognitionListener Methods --- 

    override fun onReadyForSpeech(params: Bundle?) {
        Log.d("RecordingFragment", "Ready for speech")
    }

    override fun onBeginningOfSpeech() {
        Log.d("RecordingFragment", "Beginning of speech")
        binding.textViewRecordingStatus.text = "Recording... Speak now."
    }

    override fun onRmsChanged(rmsdB: Float) { 
        // Can be used for visualizer
    }

    override fun onBufferReceived(buffer: ByteArray?) { }

    override fun onEndOfSpeech() {
        Log.d("RecordingFragment", "End of speech")
        binding.textViewRecordingStatus.text = "Processing..."
    }

    override fun onError(error: Int) {
        val errorMessage = getErrorText(error)
        Log.e("RecordingFragment", "Error: $errorMessage")
        Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_SHORT).show()
        // Navigate back or to an error screen
        findNavController().popBackStack() // Simple back navigation for now
    }

    override fun onResults(results: Bundle?) {
        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        if (!matches.isNullOrEmpty()) {
            val recognizedText = matches[0]
            Log.d("RecordingFragment", "Recognized: $recognizedText")
            // TODO: Pass recognizedText to ViewModel or directly to next fragment
            // For now, navigate to InProgress (later pass text as argument)
            findNavController().navigate(R.id.action_recordingFragment_to_inProgressFragment)
        } else {
            Log.d("RecordingFragment", "No recognition results")
            Toast.makeText(requireContext(), "Could not recognize speech", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    override fun onPartialResults(partialResults: Bundle?) {
        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        if (!matches.isNullOrEmpty()) {
            val partialText = matches[0]
            // Update UI with partial text if desired
            // binding.textViewRecordingStatus.text = partialText 
        }
    }

    override fun onEvent(eventType: Int, params: Bundle?) { }

    // Helper to get error message text
    private fun getErrorText(errorCode: Int): String {
        return when (errorCode) {
            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
            SpeechRecognizer.ERROR_CLIENT -> "Client side error"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
            SpeechRecognizer.ERROR_NETWORK -> "Network error"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
            SpeechRecognizer.ERROR_NO_MATCH -> "No speech match"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RecognitionService busy"
            SpeechRecognizer.ERROR_SERVER -> "Error from server"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
            else -> "Didn't understand, please try again."
        }
    }
}

