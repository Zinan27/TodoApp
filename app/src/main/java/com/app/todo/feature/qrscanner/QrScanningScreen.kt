@file:OptIn(ExperimentalPermissionsApi::class)

package com.app.todo.feature.qrscanner

import android.Manifest
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toAndroidRect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.aslansari.qrscaner.feature.permission.FeatureThatRequiresCameraPermission
import com.app.todo.feature.permission.NeedCameraPermissionScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.withContext

// Define a type alias for Android's size
typealias AndroidSize = android.util.Size

@Composable
@ExperimentalGetImage
fun QrScanningScreen(
    viewModel: QrScanViewModel,
    successQR: (String) -> Unit
) {
    // Collect UI state from the ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // Manage camera permission state
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    // Get the lifecycle owner for the composable
    val lifecycleOwner = LocalLifecycleOwner.current

    // Observe the lifecycle to request camera permission when the app starts
    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    cameraPermissionState.launchPermissionRequest() // Launch permission request
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer) // Remove observer on dispose
            }
        }
    )

    // Initialize the preview view and set up camera components
    val context = LocalContext.current
    val previewView = remember { PreviewView(context) }
    val preview = Preview.Builder().build()
    val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
        .setTargetResolution(
            AndroidSize(previewView.width, previewView.height) // Set target resolution for analysis
        )
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()

    // Get the target rectangle for QR code detection
    val targetRect by remember { derivedStateOf { uiState.targetRect } }

    // Launch effect to set the analyzer for the image analysis
    LaunchedEffect(targetRect) {
        imageAnalysis.setAnalyzer(
            Dispatchers.Default.asExecutor(),
            QrCodeAnalyzer(
                targetRect = targetRect.toAndroidRect(),
                previewView = previewView,
            ) { result ->
                viewModel.onQrCodeDetected(result) // Handle detected QR code
            }
        )
    }

    // Configure camera selector based on UI state
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(uiState.lensFacing)
        .build()
    var camera by remember { mutableStateOf<Camera?>(null) }

    // Launch effect to bind camera lifecycle
    LaunchedEffect(uiState.lensFacing) {
        val cameraProvider = ProcessCameraProvider.getInstance(context)
        camera = withContext(Dispatchers.IO) {
            cameraProvider.get() // Get camera provider
        }.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageAnalysis)
        preview.setSurfaceProvider(previewView.surfaceProvider) // Set surface provider for preview
    }

    // Handle camera permission UI
    FeatureThatRequiresCameraPermission(
        deniedContent = { status ->
            NeedCameraPermissionScreen(
                requestPermission = cameraPermissionState::launchPermissionRequest,
                shouldShowRationale = status.shouldShowRationale // Show rationale if needed
            )
        },
        grantedContent = {
            Scaffold { paddingValues ->
                Content(
                    modifier = Modifier.padding(paddingValues),
                    uiState = uiState,
                    previewView = previewView,
                    onTargetPositioned = viewModel::onTargetPositioned,
                    successQR = successQR // Callback for successful QR scan
                )
            }
        }
    )
}

// Composable function for displaying content on the screen
@Composable
private fun Content(
    modifier: Modifier,
    previewView: PreviewView,
    uiState: QrScanUIState,
    onTargetPositioned: (Rect) -> Unit,
    successQR: (String) -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                previewView // Display the camera preview
            }
        )

        // Set up dimensions for the QR code scanning box
        val widthInPx: Float
        val heightInPx: Float
        val radiusInPx: Float
        with(LocalDensity.current) {
            widthInPx = 250.dp.toPx() // Width of the QR box
            heightInPx = 250.dp.toPx() // Height of the QR box
            radiusInPx = 16.dp.toPx() // Corner radius for the box
        }

        // Overlay for the scanning area
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = .5f)), // Semi-transparent background
            contentAlignment = Alignment.Center,
        ) {
            Canvas(
                modifier = Modifier
                    .size(250.dp) // Size of the QR box
                    .border(1.dp, Color.White, RoundedCornerShape(16.dp)) // Border for the QR box
                    .onGloballyPositioned {
                        onTargetPositioned(it.boundsInRoot()) // Position the target for QR code
                    }
            ) {
                // Calculate offset for drawing the cutout rectangle
                val offset = Offset(
                    x = (size.width - widthInPx) / 2,
                    y = (size.height - heightInPx) / 2,
                )
                val cutoutRect = Rect(offset, Size(widthInPx, heightInPx))

                // Draw a transparent rectangle for QR code scanning
                drawRoundRect(
                    topLeft = cutoutRect.topLeft,
                    size = cutoutRect.size,
                    cornerRadius = CornerRadius(radiusInPx, radiusInPx),
                    color = Color.Transparent,
                    blendMode = BlendMode.Clear // Clear blend mode for cutout effect
                )
            }
        }

        // Handle successful QR detection
        if (uiState.detectedQR.isNotEmpty()) {
            successQR(uiState.detectedQR) // Invoke callback with detected QR code
        }
    }
}
