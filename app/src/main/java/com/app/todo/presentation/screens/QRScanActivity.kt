package com.app.todo.presentation.screens

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import com.app.todo.db.entities.TodoEntity
import com.app.todo.feature.qrscanner.QrScanViewModel
import com.app.todo.feature.qrscanner.QrScanningScreen
import com.app.todo.presentation.screens.ui.theme.ToDoTheme
import com.app.todo.presentation.viewmodels.MainViewModel
import com.app.todo.utils.AESCrypt
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint // Annotation to enable Hilt dependency injection for this activity
class QRScanActivity : ComponentActivity() {

    // ViewModels for managing QR scanning and the main application state
    private val qrScanViewModel by viewModels<QrScanViewModel>()

    private val mainViewModel by viewModels<MainViewModel>()

    @OptIn(ExperimentalGetImage::class) // Opt-in annotation for using experimental camera features
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enable edge-to-edge display for a more immersive experience
        setContent {
            ToDoTheme { // Apply the app's theme to the content
                // Set the QR scanning screen and provide a callback to handle scanned data
                QrScanningScreen(viewModel = qrScanViewModel) {
                    handleData(it) // Call the function to handle the scanned QR data
                }
            }
        }
    }

    // Function to handle the scanned QR data
    private fun handleData(qrData: String) {
        if (isValidQr(qrData)) { // Check if the QR data is valid
            finish() // Close the activity if valid
        } else {
            // Show a toast message indicating the QR data is invalid
            Toast.makeText(this, "Invalid QR", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to validate the QR data
    private fun isValidQr(qrData: String): Boolean {
        // Decrypt the QR data using the AESCrypt utility
        val plainText = AESCrypt.decrypt(qrData)

        return try {
            // Deserialize the decrypted plain text into a TodoEntity object
            val todoEntity = Gson().fromJson(plainText, TodoEntity::class.java)
            todoEntity.id = 0 // Set the ID to 0 for new entries (to avoid conflicts)
            // Add the new TodoEntity to the main ViewModel
            mainViewModel.addTodo(todoEntity)
            // Show a toast message indicating the Todo was imported successfully
            Toast.makeText(this, "Todo imported successfully", Toast.LENGTH_SHORT).show()
            true // Return true indicating the data is valid
        } catch (ex: Exception) {
            // Log the exception for debugging purposes
            Log.d("TAasdfasfasfG", ex.toString())
            false // Return false indicating the data is invalid
        }
    }

}
