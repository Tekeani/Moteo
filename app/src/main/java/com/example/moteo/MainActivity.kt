package com.example.moteo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.moteo.ui.theme.MoteoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoteoTheme {
                Surface(
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val apiService = ApiService()
                    val userPreferences = UserPreferences(this)
                    MoteoNavGraph(
                        navController = navController,
                        context = this,
                        apiService = apiService,
                        userPreferences = userPreferences
                    )
                }
            }
        }
    }
}


















