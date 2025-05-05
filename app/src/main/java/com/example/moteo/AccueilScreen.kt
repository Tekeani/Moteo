package com.example.moteo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccueilScreen(navController: NavController) {
    val backgroundPainter = painterResource(id = R.drawable.wallpaper_picture)

    var pseudo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Wallpaper
        Image(
            painter = backgroundPainter,
            contentDescription = "Wallpaper",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Motéo",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7FB3D5),
                modifier = Modifier.padding(bottom = 40.dp)
            )

            // Pseudo input
            OutlinedTextField(
                value = pseudo,
                onValueChange = { pseudo = it },
                label = { Text("Pseudo") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF7FB3D5),
                    unfocusedBorderColor = Color(0xFF7FB3D5),
                    focusedContainerColor = Color.White.copy(alpha = 0.3f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.3f),
                    focusedLabelColor = Color(0xFF7FB3D5)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mot de passe input
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Mot de passe") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF7FB3D5),
                    unfocusedBorderColor = Color(0xFF7FB3D5),
                    focusedContainerColor = Color.White.copy(alpha = 0.3f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.3f),
                    focusedLabelColor = Color(0xFF7FB3D5)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Checkbox "Se souvenir de moi"
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF7FB3D5))
                )
                Text(text = "Se souvenir de moi", color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Bouton "Se connecter"
            Button(
                onClick = {
                    // Connexion logique ici
                    navController.navigate("utilisateur")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7FB3D5)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Se connecter", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bouton "Inscription"
            Button(
                onClick = {
                    navController.navigate("inscription")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7FB3D5)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Inscription", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(40.dp))

        }

        // Nom développeuse en bas à droite
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(text = "Marie Marchal", color = Color.White, fontSize = 12.sp)
        }
    }
}


