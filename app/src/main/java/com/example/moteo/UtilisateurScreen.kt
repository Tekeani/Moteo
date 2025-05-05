package com.example.moteo

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun UtilisateurScreen(navController: NavController) {
    val backgroundPainter = painterResource(id = R.drawable.wallpaper_utilisateur)

    Box(modifier = Modifier.fillMaxSize()) {
        // Wallpaper
        Image(
            painter = backgroundPainter,
            contentDescription = "Wallpaper utilisateur",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Box avec opacité et bordure
        Surface(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(32.dp)
                .border(
                    width = 2.dp,
                    color = Color(0xFF7FB3D5),
                    shape = RoundedCornerShape(16.dp)
                ),
            color = Color.White.copy(alpha = 0.3f),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Bonjour, aujourd'hui il fait beau ! Tu peux sortir ta moto !",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}





