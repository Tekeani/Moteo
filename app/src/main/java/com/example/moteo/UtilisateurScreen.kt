package com.example.moteo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import androidx.compose.ui.text.style.TextAlign

@Composable
fun UtilisateurScreen(
    navController: NavController,
    pseudo: String,
    apiService: ApiService,
    userPreferences: UserPreferences
) {
    val backgroundPainter = painterResource(id = R.drawable.wallpaper_utilisateur)
    val coroutineScope = rememberCoroutineScope()

    var newPassword by remember { mutableStateOf("") }
    var newCity by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    var weatherMessage by remember { mutableStateOf<String?>(null) }
    var weatherError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(pseudo) {
        val profileResult = apiService.getUserProfile(pseudo)
        profileResult.fold(
            onSuccess = { profile ->
                newCity = profile.city ?: ""
                newPassword = ""
                errorMessage = null
            },
            onFailure = {
                errorMessage = "Impossible de récupérer le profil utilisateur"
            }
        )
    }

    LaunchedEffect(newCity) {
        if (newCity.isNotBlank()) {
            val result = apiService.getWeather(newCity)
            result.fold(
                onSuccess = { weatherResponse ->
                    val dangerousConditions = listOf("Rain", "Snow", "Ice", "Sleet")
                    val currentConditions = weatherResponse.weather.map { it.main }

                    weatherMessage = if (currentConditions.any { it in dangerousConditions }) {
                        "⚠️ Il ne faut pas prendre la moto aujourd'hui à $newCity : ${currentConditions.joinToString(", ")}"
                    } else {
                        "✅ Les conditions météo sont bonnes à $newCity. Vous pouvez prendre la moto."
                    }
                    weatherError = null
                },
                onFailure = {
                    weatherMessage = null
                    weatherError = "Erreur lors de la récupération de la météo pour $newCity"
                }
            )
        } else {
            weatherMessage = null
            weatherError = "Aucune ville enregistrée pour afficher la météo."
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = backgroundPainter,
            contentDescription = "Wallpaper utilisateur",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                Text(
                    text = "Bonjour $pseudo",
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth() // ✅ Ajout ici
                        .padding(16.dp)
                )
            }

            weatherMessage?.let {
                Text(
                    text = it,
                    color = if (it.contains("⚠️")) Color.Red else Color(0xFF2E7D32),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }

            weatherError?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }

            OutlinedTextField(
                value = pseudo,
                onValueChange = { },
                label = { Text("Pseudo") },
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = Color.Black,
                    disabledBorderColor = Color(0xFF7FB3D5),
                    disabledContainerColor = Color.White.copy(alpha = 0.3f),
                    disabledLabelColor = Color(0xFF7FB3D5)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("Mot de passe") },
                visualTransformation = PasswordVisualTransformation(),
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
                value = newCity,
                onValueChange = { newCity = it },
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

            errorMessage?.let {
                Text(text = it, color = Color.Red, modifier = Modifier.padding(bottom = 8.dp))
            }

            successMessage?.let {
                Text(text = it, color = Color.Green, modifier = Modifier.padding(bottom = 8.dp))
            }

            Button(
                onClick = {
                    coroutineScope.launch {
                        val user = User(pseudo, newPassword, newCity)
                        val result = apiService.updateUser(user)

                        result.fold(
                            onSuccess = { response ->
                                if (response.success) {
                                    successMessage = "Profil mis à jour avec succès"
                                    errorMessage = null
                                    userPreferences.saveUserCredentials(
                                        pseudo,
                                        newPassword,
                                        rememberMe = true,
                                        city = newCity
                                    )
                                } else {
                                    errorMessage = response.message
                                    successMessage = null
                                }
                            },
                            onFailure = {
                                errorMessage = "Erreur lors de la mise à jour"
                                successMessage = null
                            }
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7FB3D5)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Modifier", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    navController.navigate("accueil") {
                        popUpTo("utilisateur") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7FB3D5)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Déconnexion", color = Color(0xFFB22222), fontSize = 16.sp)
            }
        }
    }
}
