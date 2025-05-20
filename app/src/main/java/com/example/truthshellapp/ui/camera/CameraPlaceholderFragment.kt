package com.example.truthshellapp.ui.camera

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.truthshellapp.R
import com.example.truthshellapp.databinding.FragmentCameraPlaceholderBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.camera.core.Camera
import androidx.camera.core.FlashMode
import android.util.Size

class CameraPlaceholderFragment : Fragment() {

    private var _binding: FragmentCameraPlaceholderBinding? = null
    private val binding get() = _binding!!

    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private var flashMode = ImageCapture.FLASH_MODE_OFF
    private var camera: Camera? = null
    private lateinit var scaleGestureDetector: ScaleGestureDetector

    companion object {
        private const val TAG = "CameraPlaceholder"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val MAX_PHOTO_SIZE_BYTES = 5L * 1024 * 1024
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startCamera()
        } else {
            Toast.makeText(requireContext(), "Permission denied to access camera", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraPlaceholderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        outputDirectory = requireContext().externalMediaDirs.firstOrNull()
            ?.let { File(it, resources.getString(R.string.app_name)).apply { mkdirs() } }
            ?: requireContext().filesDir
        cameraExecutor = Executors.newSingleThreadExecutor()

        scaleGestureDetector = ScaleGestureDetector(requireContext(),
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    val currentZoom = camera?.cameraInfo?.zoomState?.value?.zoomRatio ?: 1f
                    camera?.cameraControl?.setZoomRatio(currentZoom * detector.scaleFactor)
                    return true
                }
            })
        binding.previewView.setOnTouchListener { _, event ->
            scaleGestureDetector.onTouchEvent(event)
            true
        }

        binding.buttonFlashToggle.setOnClickListener {
            flashMode = when (flashMode) {
                FlashMode.OFF -> FlashMode.ON
                FlashMode.ON -> FlashMode.AUTO
                else -> FlashMode.OFF
            }
            imageCapture?.flashMode = flashMode
            binding.buttonFlashToggle.text = "Flash: ${'$'}{flashMode.name}"
        }

        binding.buttonToggleGrid.setOnClickListener {
            binding.gridOverlay.apply {
                visibility = if (visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
        }

        binding.buttonSwitchCamera.setOnClickListener {
            lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK)
                CameraSelector.LENS_FACING_FRONT
            else CameraSelector.LENS_FACING_BACK
            startCamera()
        }

        binding.buttonTakePicture.setOnClickListener { takePhoto() }
        checkAndRequestPermission()
    }

    private fun checkAndRequestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> startCamera()
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ->
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            else -> requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }
            imageCapture = ImageCapture.Builder()
                .setFlashMode(flashMode)
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setJpegQuality(85)
                .setTargetResolution(Size(1280, 720))
                .setTargetRotation(binding.previewView.display.rotation)
                .build()
            val selector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()
            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(
                    viewLifecycleOwner, selector, preview, imageCapture
                )
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis()) + ".jpg"
        )
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    if (photoFile.length() > MAX_PHOTO_SIZE_BYTES) {
                        photoFile.delete()
                        Toast.makeText(
                            requireContext(),
                            "Image too large (>5MB), please retake",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }
                    val savedUri = Uri.fromFile(photoFile)
                    val action = CameraPlaceholderFragmentDirections
                        .actionCameraPlaceholderFragmentToInProgressFragment(
                            claimText = "",
                            audioUri = "",
                            imageUri = savedUri.toString()
                        )
                    findNavController().navigate(action)
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
        _binding = null
    }
}