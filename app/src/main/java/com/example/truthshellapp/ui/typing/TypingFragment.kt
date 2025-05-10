package com.example.truthshellapp.ui.typing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.truthshellapp.R
import com.example.truthshellapp.databinding.FragmentTypingBinding

class TypingFragment : Fragment() {

    private var _binding: FragmentTypingBinding? = null
    private val binding get() = _binding!!

    // private val viewModel: TypingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTypingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSubmit.setOnClickListener {
            val claimText = binding.editTextClaim.text.toString().trim()
            if (claimText.isNotEmpty()) {
                // TODO: Pass claimText to ViewModel or directly to next fragment
                // For now, navigate to InProgress (later pass text as argument)
                findNavController().navigate(R.id.action_typingFragment_to_inProgressFragment)
            } else {
                Toast.makeText(requireContext(), "Please enter a claim", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

