package com.example.moveup_re.view

import android.Manifest
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.moveup_re.R
import com.example.moveup_re.ui.theme.MoveUP_reTheme
import com.example.moveup_re.unit.CameraViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WorkOutView(
    cameraViewModel: CameraViewModel = hiltViewModel(LocalContext.current as ComponentActivity),
    onBackClicked: () -> Unit
){
    // Camera permission state
    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    )

    if (cameraPermissionState.status.isGranted) {
        val lifecycleOwner = LocalLifecycleOwner.current
        val cameraX by cameraViewModel.cameraX.observeAsState()
        val previewView by cameraViewModel.preview.observeAsState()

        LaunchedEffect(cameraX, cameraPermissionState.status.isGranted) {
            cameraViewModel.initialize(lifecycleOwner)
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.TopStart),
                onClick = {
                    cameraViewModel.closeCameraView()
                    onBackClicked()
                },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.pop_screen),
                )
            }
            // CameraX Preview
            previewView?.let { preview ->
                AndroidView(
                    factory = { preview },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize(0.7f)
                    )
                IconButton(
                    onClick = { cameraViewModel.takePicture() },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 50.dp)
                        .size(70.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                ){
                    Icon(
                        modifier = Modifier.size(40.dp),
                        imageVector = Icons.Filled.CameraAlt,
                        contentDescription = stringResource(R.string.take_photo)
                    )
                }
            } ?: run {
                Text(text = stringResource(R.string.not_available_camera),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
    else {
        val textToShow = if (cameraPermissionState.status.shouldShowRationale) {
            // If the user has denied the permission but the rationale can be shown,
            // then gently explain why the app requires this permission
            "The camera is important for this app. Please grant the permission."
        } else {
            // If it's the first time the user lands on this feature, or the user
            // doesn't want to be asked again for this permission, explain that the
            // permission is required
            "Camera permission required for this feature to be available. " +
                    "Please grant the permission"
        }

        Column {
            IconButton(
                modifier = Modifier.padding(top = 40.dp),
                onClick = {
                    cameraViewModel.closeCameraView()
                    onBackClicked()
                },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.pop_screen),
                )
            }
            Text("", modifier = Modifier.weight(1f))
            Text(
                text = textToShow,
                modifier = Modifier.weight(1f).padding(horizontal = 30.dp)
                )
            Button(
                onClick = {
                    cameraPermissionState.launchPermissionRequest()
                    Log.d("cameraPermission", "re-request")
                          },
                modifier = Modifier.weight(1.5f).align(Alignment.CenterHorizontally).wrapContentSize(Alignment.Center)
            ) {
                Text(text = "Request permission")
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun WorkOutScreenPreview() {
    MoveUP_reTheme {
        WorkOutView(onBackClicked = {})
    }
}