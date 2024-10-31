package com.app.todo.presentation.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import com.app.todo.feature.qrscanner.QrScanViewModel
import com.app.todo.feature.qrscanner.QrScanningScreen
import com.app.todo.presentation.screens.ui.theme.ToDoTheme
import com.app.todo.utils.AESCrypt

class QRScanActivity : ComponentActivity() {
    private val qrScanViewModel by viewModels<QrScanViewModel>()

    @OptIn(ExperimentalGetImage::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoTheme {
                QrScanningScreen(viewModel = qrScanViewModel) {
                    handleData(it)
                }
            }
        }
    }

    private fun handleData(qrData: String) {
        val data = isValidQr(qrData)
        if (data.second) {
            setResults(data.first)
            finish()
        } else {
            Toast.makeText(this, "Invalid QR", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isValidQr(qrData: String): Pair<String, Boolean> {
        val plainText = AESCrypt.decrypt(qrData)
        return Pair(plainText ?: "", plainText != null)
    }

    private fun setResults(data: String) {
        val intent = Intent()
        intent.putExtra(QR_DATA, data)
        setResult(RESULT_OK, intent)
        finish()
    }

    companion object {
        const val QR_DATA = "qr_data"
    }
}
