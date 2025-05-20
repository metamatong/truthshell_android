package com.example.truthshellapp.ui.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.truthshellapp.data.local.AppDatabase
import com.example.truthshellapp.data.local.SavedResultEntity
import com.example.truthshellapp.databinding.FragmentSavedResultsBinding
import com.example.truthshellapp.R
import kotlinx.coroutines.launch

/**
 * Displays a list of saved TruthShell analysis results from local storage.
 */
class SavedResultsFragment : Fragment() {

    private var _binding: FragmentSavedResultsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SavedResultsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = SavedResultsAdapter()
        binding.recyclerViewSavedResults.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@SavedResultsFragment.adapter
        }
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
        loadSavedResults()
    }

    private fun loadSavedResults() {
        lifecycleScope.launch {
            val results: List<SavedResultEntity> = AppDatabase
                .getInstance(requireContext())
                .savedResultDao()
                .getAll()
            adapter.submitList(results)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}