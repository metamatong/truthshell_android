package com.example.truthshellapp.ui.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.truthshellapp.R
import com.example.truthshellapp.databinding.FragmentLandingBinding

class LandingFragment : Fragment() {

    private var _binding: FragmentLandingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLandingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonCapture.setOnClickListener {
            findNavController().navigate(
                R.id.action_landingFragment_to_cameraPlaceholderFragment
            )
        }

        binding.buttonRecord.setOnClickListener {
            findNavController().navigate(R.id.action_landingFragment_to_recordingFragment)
        }

        binding.buttonType.setOnClickListener {
            findNavController().navigate(R.id.action_landingFragment_to_typingFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

