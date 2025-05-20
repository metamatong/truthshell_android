package com.example.truthshellapp.ui.inprogress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.truthshellapp.R
import com.example.truthshellapp.databinding.FragmentInProgressBinding
import com.example.truthshellapp.BuildConfig
import com.example.truthshellapp.ui.inprogress.InProgressFragmentDirections
import android.net.Uri
import java.io.File
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class InProgressFragment : Fragment() {

    private var _binding: FragmentInProgressBinding? = null
    private val binding get() = _binding!!

    private val viewModel: InProgressViewModel by viewModels()
    private val args: InProgressFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiKey = BuildConfig.SERVER_API_KEY

        // Observe API state changes before starting the request
        observeViewModel()
        if (args.audioUri.isNotEmpty()) {
            // Analyze recorded audio file
            val audioFile = File(Uri.parse(args.audioUri).path ?: "")
            val requestFile = audioFile.asRequestBody("audio/mp4".toMediaTypeOrNull())
            val filePart = MultipartBody.Part.createFormData("file", audioFile.name, requestFile)
            viewModel.analyzeFileWithApi(apiKey, "audio", filePart)
        } else if (args.claimText.isNotEmpty()) {
            // Analyze typed claim
            viewModel.analyzeTextWithApi(apiKey, args.claimText)
        } else {
            Toast.makeText(requireContext(), "Error: No input provided", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    private fun observeViewModel() {
        viewModel.apiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                ApiState.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.textViewInProgressStatus.text = getString(R.string.in_progress_message)
                }
                ApiState.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    val result = viewModel.sonarResult.value
                    if (result != null && result.confidenceScore != null && result.confidenceLabel != null) {
                        // Navigate to Results Fragment
                        val sourcesArray = result.sources?.map { it.url }?.toTypedArray() ?: emptyArray()
                        val action = InProgressFragmentDirections.actionInProgressFragmentToResultsFragment(
                            originalClaim = args.claimText, // Pass original claim
                            confidenceScore = result.confidenceScore,
                            confidenceLabel = result.confidenceLabel,
                            sources = sourcesArray
                        )
                        findNavController().navigate(action)
                    } else {
                        // Handle unexpected success state (missing data)
                        Toast.makeText(requireContext(), "API Success but data missing", Toast.LENGTH_SHORT).show()
                        val actionError = InProgressFragmentDirections.actionInProgressFragmentToErrorFragment()
                        findNavController().navigate(actionError)
                    }
                }
                ApiState.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    val errorMessage = viewModel.errorMessage.value ?: "An unknown error occurred"
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                    // Navigate to Error Fragment
                    val actionError = InProgressFragmentDirections.actionInProgressFragmentToErrorFragment()
                    findNavController().navigate(actionError)
                }
                null -> { /* Initial state, do nothing */ }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

