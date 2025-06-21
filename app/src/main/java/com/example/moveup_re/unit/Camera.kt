package com.example.moveup_re.unit

import android.app.Application
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
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import javax.inject.Inject

interface CameraCapture  {
    fun initialize(lifecycleOwner: LifecycleOwner)
    fun getPreviewView() : PreviewView
    fun takePicture(onImageCaptured: (Uri) -> Unit)
    fun unBindCamera()
}

@Singleton
class CameraCaptureImpl  @Inject constructor(
    private val context: Context,
) : CameraCapture  {
    private lateinit var previewView: PreviewView
    private lateinit var cameraController: LifecycleCameraController

    // 카메라 초기화
    override fun initialize(lifecycleOwner: LifecycleOwner) {
        // PreviewView, cameraController 초기화
        previewView = PreviewView(context)
        cameraController = LifecycleCameraController(context)
        // 카메라 생명 주기 바인딩
        cameraController.bindToLifecycle(lifecycleOwner)
        // 전면 카메라 설정
        cameraController.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
        // 컨트롤러 연결
        previewView.controller = cameraController
    }

    // 프리뷰 반환
    override fun getPreviewView(): PreviewView {
        return previewView
    }

    // 촬영
    override fun takePicture(onImageCaptured: (Uri) -> Unit) {
        // 캡쳐를 위한 변수 정의
        val contentValues = ContentValues().apply {
            // 파일 이름 지정
            put(MediaStore.MediaColumns.DISPLAY_NAME, "${System.currentTimeMillis()}.jpg")
            // 사진 압축 타입 지정
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            // 저장 가능한 버전인지 확인후, 저장 경로 지정
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }
        // 사진을 파일에 저장 하기 위한 옵션 정의
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
        ).build()

        // 사진을 파일에 직접 저장
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

    // 카메라 해체
    override fun unBindCamera() {
        cameraController.unbind()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object CameraModule {
    @Provides
    @Singleton
    fun provideCameraCapture(
        @ApplicationContext context: Context,
    ): CameraCapture {
        return CameraCaptureImpl(context)
    }
}

@HiltViewModel
class CameraViewModel @Inject constructor(
    application: Application,
    cameraCapture: CameraCapture,
) : AndroidViewModel(application) {

    private val _cameraX = MutableLiveData<CameraCapture?>(null)
    val cameraX: LiveData<CameraCapture?> = _cameraX

    private val _preview = MutableLiveData<PreviewView?>(null)
    val preview: LiveData<PreviewView?> = _preview

    private val _showCameraView = MutableLiveData<Boolean>(false)
    val showCameraView: LiveData<Boolean> = _showCameraView

    init {
        _cameraX.value = cameraCapture
    }

    fun initialize(lifecycleOwner: LifecycleOwner) {
        cameraX.value?.let { capture ->
            capture.initialize(lifecycleOwner)
            _preview.value = capture.getPreviewView()
        }
    }

    fun takePicture() {
        cameraX.value?.takePicture { uri ->
        }
    }

    fun showCameraView() {
        _showCameraView.value = true
    }

    fun closeCameraView() {
        cameraX.value?.unBindCamera()
        _showCameraView.value = false
        _preview.value = null
    }
}