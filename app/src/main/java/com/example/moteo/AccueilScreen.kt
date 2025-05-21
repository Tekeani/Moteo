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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccueilScreen(
    navController: NavController,
    apiService: ApiService,
    userPreferences: UserPreferences
) {
    val backgroundPainter = painterResource(id = R.drawable.wallpaper_picture)
    val coroutineScope = rememberCoroutineScope()

    var pseudo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Charger les identifiants si "Se souvenir de moi"
    LaunchedEffect(Unit) {
        userPreferences.userCredentialsFlow.firstOrNull()?.let { saved ->
            if (saved.rememberMe) {
                pseudo = saved.pseudo
                password = saved.password
                rememberMe = saved.rememberMe
            }
        }
    }

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
                text = "Motéo",
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

            errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    when {
                        pseudo.isBlank() -> errorMessage = "Le pseudo est obligatoire"
                        password.isBlank() -> errorMessage = "Le mot de passe est obligatoire"
                        else -> {
                            errorMessage = null
                            isLoading = true
                            coroutineScope.launch {
                                // Crée un objet UserLoginRequest si tu en as un, sinon passe directement les Strings
                                val result = apiService.loginUser(pseudo, password)

                                isLoading = false

                                result.fold(
                                    onSuccess = { response ->
                                        if (response.success) {
                                            userPreferences.saveUserCredentials(
                                                pseudo,
                                                password,
                                                rememberMe,
                                                "" // City non utilisé ici
                                            )
                                            // Ici on passe bien le pseudo (String), pas un objet
                                            navController.navigate("utilisateur/$pseudo") {
                                                popUpTo("accueil") { inclusive = true }
                                            }
                                        } else {
                                            errorMessage = response.message
                                        }
                                    },
                                    onFailure = { error ->
                                        errorMessage = "Erreur : ${error.message ?: "Connexion impossible"}"
                                    }
                                )
                            }
                        }
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
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Se connecter", color = Color.White, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("inscription") },
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
