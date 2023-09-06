package com.example.go2life.view.navigation.reels

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.MediaRecorder
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.FLASH_MODE_AUTO
import androidx.camera.core.ImageCapture.FLASH_MODE_OFF
import androidx.camera.core.ImageCapture.FLASH_MODE_ON
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.example.go2life.R
import com.example.go2life.base.BaseFragment
import com.example.go2life.databinding.FragmentCustomReelsCameraBinding
import com.example.go2life.utils.gone
import com.example.go2life.utils.toast
import com.example.go2life.utils.visible
import com.example.go2life.view.navigation.MainActivity
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.util.concurrent.ExecutionException


/**
 * A Fragment that provides a custom camera interface for recording videos and taking pictures.
 * This Fragment allows users to switch between the front and back cameras, control the flash,
 * and record videos by holding a record button.
 *
 * @constructor Creates an instance of [CustomReelsCameraFragment].
 */

@Suppress("DEPRECATION")
class CustomReelsCameraFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentCustomReelsCameraBinding
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraManager: CameraManager
    private var cameraId: String? = null
    private lateinit var cameraSelector: CameraSelector
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var preview: Preview
    private lateinit var flipAnimation: Animation
    private var flashMode = FLASH_MODE_OFF
    private lateinit var imageCapture: ImageCapture
    private lateinit var mediaRecorder: MediaRecorder
    private lateinit var countDownTimer: CountDownTimer
    private val maxRecordingTimeInSeconds = 90 // Maximum recording time: 1 minute 30 seconds
    private val intervalInSeconds = 1
    private var videoFilePath: String? = null
    private var videoFile: File? = null
    private var isRecording = false




    /**
     * Initialize the Fragment's view and start the camera.
     *
     * @param inflater The LayoutInflater to inflate the view.
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState The previously saved state if the fragment is being re-initialized.
     * @return The root view of the fragment.
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // ... (Initializing UI elements, handling touch events, and preparing MediaRecorder)
        binding = FragmentCustomReelsCameraBinding.inflate(inflater, container, false)
        binding.onClick = this
        startCamera()
        mediaRecorder = MediaRecorder()
        cameraManager = requireContext().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        setupUi()
        return binding.root
    }



    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("ClickableViewAccessibility")
    private fun setupUi() {
        binding.btnCameraRecord.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // User pressed the button, start recording
                    if (ContextCompat.checkSelfPermission(
                            requireContext(), Manifest.permission.READ_MEDIA_VIDEO
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        startVideoRecording()
                    } else {
                        requestAudioPermissions()
                    }
                    true // Return true to indicate that you have consumed the touch event
                }

                MotionEvent.ACTION_UP -> {
                    // User released the button, stop recording
                    stopVideoRecording()
                    true // Return true to indicate that you have consumed the touch event
                }

                else -> false // Return false for other touch events
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCameraFlash.setImageResource(R.drawable.ic_flash_off)
        flipAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_in)

    }

    private fun updateTimerText(remainingTimeInSeconds: Int) {
        binding.tvTimer.text = String.format("00:%02d", remainingTimeInSeconds)
    }

    private fun startVideoRecording() {
        if (!isRecording) {
            val outputDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).absolutePath// Function to get a directory to save videos
            val fileName = "${System.currentTimeMillis()}.mp4"
            videoFile = File(outputDirectory, fileName)
            videoFilePath = videoFile!!.absolutePath
            mediaRecorder.apply {
                try {
                    setAudioSource(MediaRecorder.AudioSource.CAMCORDER)
                    setVideoSource(MediaRecorder.VideoSource.CAMERA)
                    setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    setOutputFile(videoFilePath)
                    setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                    setMaxDuration(maxRecordingTimeInSeconds * 1000)

                    try {
                        prepare()
                        start()
                        startCountdownTimer()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        // Handle exceptions here, e.g., show an error message to the user.
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    // Handle exceptions here, e.g., show an error message to the user.
                }
            }
        }
    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireContext().getExternalFilesDir(null)?.let {
            File(it, "media").apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireContext().filesDir
    }


    private fun stopVideoRecording() {
        if (isRecording) {
            try {
                mediaRecorder.stop()
                countDownTimer.cancel()
            } catch (e: Exception) {
                e.printStackTrace()
                requireContext().toast(e.message.toString())
                // Handle the stop error here, e.g., show an error message to the user.
            }
        } else {
            // Reset and release the MediaRecorder
            mediaRecorder.reset()
            mediaRecorder.release()
            if (videoFile != null) {
                // Save the recorded video to the gallery
                val values = ContentValues().apply {
                    put(
                        MediaStore.Video.Media.DISPLAY_NAME,
                        "MyVideo.mp4"
                    ) // You can change the file name as needed
                    put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
                }
                val contentResolver = requireActivity().contentResolver
                val uri =
                    contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)

                // Move the recorded video file to the gallery URI
                videoFile?.let { file ->
                    val outputStream = contentResolver.openOutputStream(uri!!)
                    val inputStream = file.inputStream()
                    outputStream?.use { output ->
                        inputStream.use { input ->
                            input.copyTo(output)
                        }
                    }
                    outputStream?.close()
                }

                // Notify the MediaScanner to scan the new video so it appears in the gallery
                MediaScannerConnection.scanFile(
                    requireContext(),
                    arrayOf(videoFile?.absolutePath),
                    arrayOf("video/mp4"),
                    null
                )

                val bundle = Bundle()
                bundle.putString("videoFile", videoFilePath)
                (requireActivity() as MainActivity).goneIcon()

                // Navigate to another screen and pass the bundle
                findNavController().navigate(
                    R.id.action_customReelsCameraFragment_to_previewReelsCamera,
                    bundle
                )
            } else {
                // Handle the case where videoFile is null (e.g., an error occurred during recording)
                // You can show an error message to the user.
            }
        }
    }


    private fun startCountdownTimer() {
        countDownTimer = object : CountDownTimer(
            (maxRecordingTimeInSeconds * 1000).toLong(),
            (intervalInSeconds * 1000).toLong()
        ) {
            override fun onTick(millisUntilFinished: Long) {
                val remainingSeconds = (millisUntilFinished / 1000).toInt()
                updateTimerText(remainingSeconds)
            }

            override fun onFinish() {
                stopVideoRecording()
            }
        }

        countDownTimer.start()
    }


    private val requestVideoPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openGalleryForVideos()
            } else {
                showPermissionDeniedDialog()
            }
        }

    private val requestAudioPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startVideoRecording()
            } else {
                showPermissionDeniedDialog()
            }
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestVideoPermissions() {
        val permission = Manifest.permission.READ_MEDIA_VIDEO
        if (ContextCompat.checkSelfPermission(requireContext(), permission)
            == PackageManager.PERMISSION_GRANTED
        ) {
            openGalleryForVideos()
        } else {
            // Request the permission
            requestVideoPermissionLauncher.launch(permission)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestAudioPermissions() {
        val permission = Manifest.permission.RECORD_AUDIO
        if (ContextCompat.checkSelfPermission(requireContext(), permission)
            == PackageManager.PERMISSION_GRANTED
        ) {
            startVideoRecording()
        } else {
            // Request the permission
            requestAudioPermissionLauncher.launch(permission)
        }
    }

    private fun startCamera() {
        (requireActivity() as MainActivity).goneIcon()
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            try {
                cameraProvider = cameraProviderFuture.get()
                bindCameraPreview(cameraProvider)
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun bindCameraPreview(cameraProvider: ProcessCameraProvider) {
        preview = Preview.Builder().build()
        cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()
        preview.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview)
    }

    private val pickVideoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                // Handle the selected video here (e.g., get the video URI)
                val selectedVideoUri: Uri? = data?.data
                if (selectedVideoUri != null) {
                    val bundle = Bundle()
                    bundle.putString("selectedVideoUri", selectedVideoUri.toString())
                    (requireActivity() as MainActivity).goneIcon()
                    // Navigate to another screen and pass the bundle
                    findNavController().navigate(
                        R.id.action_customReelsCameraFragment_to_previewReelsCamera,
                        bundle
                    )
                }
            }
        }

    private fun openGalleryForVideos() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "video/*"
        pickVideoLauncher.launch(intent)
    }

    private fun showPermissionDeniedDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.apply {
            setTitle(getString(R.string.storage_permission_denied))
            setMessage(getString(R.string.storage_permission_denied_message))
            setPositiveButton(getString(R.string.go_to_settings)) { dialogInterface, _ ->
                dialogInterface.dismiss()
                openAppSettings()
            }
            setNegativeButton(getString(R.string.cancel)) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            setCancelable(false)
        }
        alertDialogBuilder.create().show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        startActivity(intent)
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onClick(v: View?) {
        when (v) {
            binding.btnCameraChange -> toggleCamera()
            binding.btnCameraBack -> findNavController().popBackStack()
            binding.btnCameraMedia -> {
                if (ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.READ_MEDIA_VIDEO
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    openGalleryForVideos()
                } else {
                    requestVideoPermissions()
                }
            }

            binding.btnCameraFlash -> {
                toggleFlashMode()
            }

        }
    }


    private fun toggleFlashMode() {
        try {
            flashMode = when (flashMode) {
                FLASH_MODE_OFF -> {
                    // Turn on the flash
                    binding.btnCameraFlash.setImageResource(R.drawable.ic_flash_on)
                    flashMode = FLASH_MODE_ON
                    toggleFlashlight() // Turn on the flashlight
                    FLASH_MODE_ON
                }

                FLASH_MODE_ON -> {
                    // Set flash to auto
                    binding.btnCameraFlash.setImageResource(R.drawable.ic_flash_auto)
                    flashMode = FLASH_MODE_AUTO
                    toggleFlashlight() // Set the flashlight to auto mode
                    FLASH_MODE_AUTO
                }

                FLASH_MODE_AUTO -> {
                    // Turn off the flash
                    binding.btnCameraFlash.setImageResource(R.drawable.ic_flash_off)
                    flashMode = FLASH_MODE_OFF
                    toggleFlashlight() // Turn off the flashlight
                    FLASH_MODE_OFF
                }

                else -> FLASH_MODE_OFF
            }

            // Apply the flash mode to the imageCapture
            imageCapture.flashMode = flashMode
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle exceptions here, e.g., show an error message to the user.
        }
    }

    private fun toggleFlashlight() {
        try {
            // Ensure that the cameraId is not null before proceeding
            cameraId?.let { id ->
                val cameraManager = requireContext().getSystemService(Context.CAMERA_SERVICE) as CameraManager

                val characteristics = cameraManager.getCameraCharacteristics(id)
                val flashAvailable = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)

                if (flashAvailable == true) {
                    cameraManager.setTorchMode(id, flashMode == FLASH_MODE_ON)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle exceptions here, e.g., show an error message to the user.
        }
    }

    private fun isFrontCameraFlashAvailable(context: Context): Boolean {
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraIds = cameraManager.cameraIdList

        for (cameraId in cameraIds) {
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            val lensFacing = characteristics.get(CameraCharacteristics.LENS_FACING)
            val flashAvailable = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)

            if (lensFacing == CameraCharacteristics.LENS_FACING_FRONT && flashAvailable == true) {
                return true
            }
        }

        return false
    }

    /**
     * Toggle the camera lens between front and back cameras.
     */
    private fun toggleCamera() {
        // Toggle the camera lens facing
        cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            if (!isFrontCameraFlashAvailable(requireContext())) {
                binding.btnCameraFlash.gone()
            }
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            binding.btnCameraFlash.visible()
            CameraSelector.DEFAULT_BACK_CAMERA
        }

        // Update the cameraId based on the new cameraSelector
        cameraId = cameraManager.cameraIdList.firstOrNull { id ->
            val characteristics = cameraManager.getCameraCharacteristics(id)
            val lensFacing = characteristics.get(CameraCharacteristics.LENS_FACING)
            return@firstOrNull (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA && lensFacing == CameraCharacteristics.LENS_FACING_BACK) ||
                    (cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA && lensFacing == CameraCharacteristics.LENS_FACING_FRONT)
        }

        // Rebind the camera with the new selector
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, cameraSelector, preview)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Stop video recording, save the recorded video, and navigate to another screen.
     */

}
