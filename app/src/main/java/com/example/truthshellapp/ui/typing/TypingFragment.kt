package com.example.truthshellapp.ui.typing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
                val action = TypingFragmentDirections
                    .actionTypingFragmentToInProgressFragment(
                        claimText = claimText,
                        audioUri  = ""
                    )
                findNavController().navigate(action)
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

