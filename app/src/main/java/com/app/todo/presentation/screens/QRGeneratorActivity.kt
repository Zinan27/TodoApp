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
        enableEdgeToEdge()
        setContent {
            ToDoTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        bitmap = getQrCodeBitmap(getQrData()).asImageBitmap(),
                        contentDescription = "QR code",
                        modifier = Modifier
                            .size(200.dp)
                    )
                    HeadingText(
                        text = "Scan QR to import a Task",
                        modifier = Modifier.padding(top = 20.dp)
                    )
                }
            }
        }
    }

    private fun getQrCodeBitmap(str: String): Bitmap {
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.encodeBitmap(
            str,
            BarcodeFormat.QR_CODE,
            300,
            300
        )
    }

    private fun getQrData(): String {
        val data = intent.getStringExtra(TODO_DATA)
        data ?: finish()
        val cipherText = AESCrypt.encrypt(data)
        cipherText ?: finish()
        Log.d("TAasdfasfasfG", "getQrData: $cipherText")
        return cipherText
    }

    companion object {
        const val TODO_DATA = "TODO_DATA"
    }
}

