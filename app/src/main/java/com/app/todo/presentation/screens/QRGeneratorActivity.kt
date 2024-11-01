package com.app.todo.presentation.screens

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.app.todo.presentation.HeadingText
import com.app.todo.presentation.screens.ui.theme.ToDoTheme
import com.app.todo.utils.AESCrypt
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class QRGeneratorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enables edge-to-edge display for immersive experience

        // Set the content view using Jetpack Compose
        setContent {
            ToDoTheme { // Apply the app's theme
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally, // Center horizontally
                    verticalArrangement = Arrangement.Center // Center vertically
                ) {
                    // Display the generated QR code image
                    Image(
                        bitmap = getQrCodeBitmap(getQrData()).asImageBitmap(),
                        contentDescription = "QR code", // Content description for accessibility
                        modifier = Modifier.size(200.dp) // Set size of the QR code
                    )
                    // Display heading text below the QR code
                    HeadingText(
                        text = "Scan QR to import a Task",
                        modifier = Modifier.padding(top = 20.dp) // Add padding above the heading
                    )
                }
            }
        }
    }

    // Generate a QR code bitmap from a given string
    private fun getQrCodeBitmap(str: String): Bitmap {
        val barcodeEncoder = BarcodeEncoder() // Create an instance of BarcodeEncoder
        // Encode the string into a QR code bitmap
        return barcodeEncoder.encodeBitmap(
            str,
            BarcodeFormat.QR_CODE, // Specify the barcode format as QR_CODE
            300, // Width of the generated QR code
            300 // Height of the generated QR code
        )
    }

    // Retrieve and encrypt the QR data from the intent
    private fun getQrData(): String {
        // Get the TODO_DATA string extra from the intent
        val data = intent.getStringExtra(TODO_DATA)
        data ?: finish() // Finish the activity if data is null

        // Encrypt the retrieved data
        val cipherText = AESCrypt.encrypt(data)
        cipherText ?: finish() // Finish the activity if encryption fails

        Log.d("TAasdfasfasfG", "getQrData: $cipherText") // Log the encrypted data for debugging
        return cipherText // Return the encrypted data
    }

    companion object {
        const val TODO_DATA = "TODO_DATA" // Key for accessing TODO data in the intent
    }
}
