package com.example.moteo

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InscriptionScreen(
    navController: NavController,
    apiService: ApiService,
    userPreferences: UserPreferences
) {
    val backgroundPainter = painterResource(id = R.drawable.wallpaper_picture)
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var pseudo by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val rememberMe = true

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
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
                text = "Inscription",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7FB3D5),
                modifier = Modifier.padding(bottom = 40.dp)
            )

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

            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("Ville") },
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

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmer mot de passe") },
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

            Spacer(modifier = Modifier.height(24.dp))

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Button(
                onClick = {
                    coroutineScope.launch {
                        if (password != confirmPassword) {
                            errorMessage = "Les mots de passe ne correspondent pas"
                            return@launch
                        }
                        if (pseudo.isBlank()) {
                            errorMessage = "Le pseudo est obligatoire"
                            return@launch
                        }
                        if (city.isBlank()) {
                            errorMessage = "La ville est obligatoire"
                            return@launch
                        }
                        if (password.isBlank()) {
                            errorMessage = "Le mot de passe est obligatoire"
                            return@launch
                        }

                        isLoading = true
                        errorMessage = null

                        val user = User(pseudo = pseudo, password = password, city = city)
                        val result = apiService.registerUser(user)

                        if (result.isSuccess) {
                            Toast.makeText(context, "Inscription réussie", Toast.LENGTH_SHORT).show()
                            userPreferences.saveUserCredentials(pseudo, password, rememberMe, city)
                            // Navigation avec le pseudo en paramètre (string)
                            navController.navigate("utilisateur/$pseudo") {
                                popUpTo("inscription") { inclusive = true }
                            }
                        } else {
                            errorMessage = "Échec de l'inscription: ${result.exceptionOrNull()?.message ?: "Erreur inconnue"}"
                        }

                        isLoading = false
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7FB3D5)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("S'inscrire", color = Color.White, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = {
                    navController.navigate("accueil") {
                        popUpTo("inscription") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Déjà un compte ? Se connecter", color = Color(0xFF7FB3D5))
            }
        }
    }
}
