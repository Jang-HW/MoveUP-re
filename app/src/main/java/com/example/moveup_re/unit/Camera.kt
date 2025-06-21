package com.example.moveup_re.unit

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import jakarta.inject.Singleton
import javax.inject.Inject

interface Camera {
    fun initialize(lifecycleOwner: LifecycleOwner)
    fun getPreviewView() : PreviewView
    fun takePicture(onImageCaptured: (Uri) -> Unit)
    fun unBindCamera()
}

@Singleton
class CameraImpl @Inject constructor(
    private val context: Context,
) : Camera {
    private lateinit var previewView: PreviewView
    private lateinit var cameraController: LifecycleCameraController

    override fun initialize(lifecycleOwner: LifecycleOwner) {
        previewView = PreviewView(context)
        cameraController = LifecycleCameraController(context)
        cameraController.bindToLifecycle(lifecycleOwner)
        cameraController.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
        previewView.controller = cameraController
    }

    override fun getPreviewView(): PreviewView {
        return previewView
    }

    override fun takePicture(onImageCaptured: (Uri) -> Unit) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "${System.currentTimeMillis()}.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
        ).build()

        cameraController.takePicture(outputOptions, ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    outputFileResults.savedUri?.let {
                        onImageCaptured(it)
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraCaptureManager", "Photo capture failed: ${exception.message}")
                }
            })
    }

    override fun unBindCamera() {
        cameraController.unbind()
    }
}