package com.example.truthshellapp.ui.results

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.truthshellapp.data.local.SavedResultEntity
import com.example.truthshellapp.data.local.AppDatabase
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.truthshellapp.R
import com.example.truthshellapp.databinding.FragmentResultsBinding

class ResultsFragment : Fragment() {

    private var _binding: FragmentResultsBinding? = null
    private val binding get() = _binding!!

    private val args: ResultsFragmentArgs by navArgs()
    private lateinit var sourcesAdapter: SourcesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        displayResults()
        setupButtons()
    }

    private fun setupRecyclerView() {
        sourcesAdapter = SourcesAdapter(args.sources.toList())
        binding.recyclerViewSources.apply {
            adapter = sourcesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun displayResults() {
        binding.textViewConfidenceScore.text = "${args.confidenceScore}%"
        binding.textViewConfidenceLabel.text = args.confidenceLabel
        // Adapter already has the sources from setupRecyclerView
    }

    private fun setupButtons() {
        binding.buttonBack.setOnClickListener {
            // Navigate back to landing, clearing the stack
            findNavController().navigate(R.id.action_resultsFragment_to_landingFragment)
        }

        binding.buttonSave.setOnClickListener {
            val resultEntity = SavedResultEntity(
                originalClaim = args.originalClaim,
                confidenceScore = args.confidenceScore,
                confidenceLabel = args.confidenceLabel,
                sources = args.sources.toList(),
                timestamp = System.currentTimeMillis()
            )
            lifecycleScope.launch {
                AppDatabase.getInstance(requireContext()).savedResultDao().insert(resultEntity)
                Toast.makeText(requireContext(), "Result saved", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonShare.setOnClickListener {
            // Implement share functionality using the original claim passed via args
            val shareText = "TruthShell Result:\nClaim: ${args.originalClaim}\nConfidence: ${args.confidenceScore}% (${args.confidenceLabel})\nSources: ${args.sources.joinToString("\n")}"

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

