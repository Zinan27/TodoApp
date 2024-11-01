package com.app.todo.feature.qrscanner

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

// ViewModel for QR code scanning functionality
class QrScanViewModel : ViewModel() {
    // Mutable state flow to hold the UI state for QR scanning
    private val _uiState: MutableStateFlow<QrScanUIState> = MutableStateFlow(QrScanUIState())
    // Public state flow to expose UI state to the Composable
    val uiState: StateFlow<QrScanUIState> = _uiState

    // Function to handle detected QR codes
    fun onQrCodeDetected(result: String) {
        Log.d("QR Scanner", result) // Log the detected QR code result
        // Update the UI state with the detected QR code
        _uiState.update { it.copy(detectedQR = result) }
    }

    // Function to update the position of the target rectangle
    fun onTargetPositioned(rect: Rect) {
        // Update the UI state with the new target rectangle position
        _uiState.update { it.copy(targetRect = rect) }
    }
}

// Data class representing the UI state for the QR scanning screen
data class QrScanUIState(
    val loading: Boolean = false, // Indicates loading state
    val detectedQR: String = "", // Holds the detected QR code string
    val targetRect: Rect = Rect.Zero, // Holds the position of the target rectangle for scanning
    val lensFacing: Int = CameraSelector.LENS_FACING_BACK, // Specifies which camera lens to use (front or back)
)
