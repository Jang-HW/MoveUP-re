package com.example.moveup_re.screen

import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.moveup_re.R
import com.example.moveup_re.ui.theme.MoveUP_reTheme

@Composable
fun WorkOutScreen(
    onImageCaptureClick: () -> Unit,
    onVideoCaptureClick: () -> Unit
){
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // CameraX Preview
        AndroidView(
            factory = { context ->
                PreviewView(context).apply {
                    layoutParams = android.view.ViewGroup.LayoutParams(
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // 하단 버튼 Row
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onImageCaptureClick,
                modifier = Modifier
                    .size(70.dp)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        shape=CircleShape
                    )
            ){
                Icon(
                    modifier = Modifier.size(40.dp),
                    imageVector = Icons.Filled.CameraAlt,
                    contentDescription = stringResource(R.string.take_photo)
                )
            }

            Text(text = "", modifier = Modifier.width(80.dp))

            IconButton(
                onClick = onVideoCaptureClick,
                modifier = Modifier
                    .size(70.dp)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        shape=CircleShape
                    )
            ){
                Icon(
                    modifier = Modifier.size(40.dp),
                    imageVector = Icons.Filled.Videocam,
                    contentDescription = stringResource(R.string.start_capture)
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun WorkOutScreenPreview() {
    MoveUP_reTheme {
        WorkOutScreen(
            onImageCaptureClick = {},
            onVideoCaptureClick = {}
        )
    }
}