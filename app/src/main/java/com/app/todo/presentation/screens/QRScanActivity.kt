package com.app.todo.presentation.screens

import android.content.Intent
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

@AndroidEntryPoint
class QRScanActivity : ComponentActivity() {

    private val qrScanViewModel by viewModels<QrScanViewModel>()
    private val mainViewModel by viewModels<MainViewModel>()

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
        if (isValidQr(qrData)) {
            finish()
        } else {
            Toast.makeText(this, "Invalid QR", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isValidQr(qrData: String): Boolean {
        val plainText = AESCrypt.decrypt(qrData)

        return try {
            val todoEntity = Gson().fromJson(plainText, TodoEntity::class.java)
            todoEntity.id = 0
            mainViewModel.addTodo(todoEntity)
            Toast.makeText(this, "Todo imported successfully", Toast.LENGTH_SHORT).show()
            true
        } catch (ex: Exception) {
            Log.d("TAasdfasfasfG", ex.toString())
            false
        }

    }

}
