package com.example.csproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.csproject.ui.theme.CSProjectTheme

class NavHostMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CSProjectTheme {
                MainApp()
            }
        }
    }
}